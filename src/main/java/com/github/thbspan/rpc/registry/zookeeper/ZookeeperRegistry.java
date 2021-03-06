package com.github.thbspan.rpc.registry.zookeeper;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;

import com.github.thbspan.rpc.common.logger.Logger;
import com.github.thbspan.rpc.common.logger.LoggerFactory;
import com.github.thbspan.rpc.invoker.Invoker;
import com.github.thbspan.rpc.protocol.DubboProtocol;
import com.github.thbspan.rpc.protocol.HttpProtocol;
import com.github.thbspan.rpc.protocol.Protocol;
import com.github.thbspan.rpc.protocol.RmiProtocol;
import com.github.thbspan.rpc.registry.Registry;

public class ZookeeperRegistry implements Registry {
    private static final Logger logger = LoggerFactory.getLogger(ZookeeperRegistry.class);

    private final String ip;
    private final int port;
    private CuratorFramework client;

    public ZookeeperRegistry(String ip, int port) {
        this.ip = ip;
        this.port = port;
        init();
    }

    private void init() {
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
                .connectString(ip + ':' + port)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .connectionTimeoutMs(5000);

        client = builder.build();
        client.start();
        try {
            client.blockUntilConnected();
        } catch (InterruptedException e) {
            throw new IllegalStateException("init zookeeper exception", e);
        }
    }

    @Override
    public void registry(Protocol protocol, Invoker invoker) {
        String serviceName = invoker.getInterfaceClass().getName();
        String pathProvider = protocol.getPathProvider(serviceName);
        String url = protocol.getServiceUrl(serviceName);
        try {
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(getPath(url, pathProvider));
        } catch (Exception e) {
            logger.error("registry protocol exception", e);
        }
    }

    private String getPath(String url, String pathProvider) {
        try {
            return pathProvider + "/" + URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public void subscribe(Protocol protocol, String serviceName, List<Invoker> invokers) {
        String pathProvider = protocol.getPathProvider(serviceName);
        try {
            Watcher watcher = event -> update(pathProvider, invokers);
            List<String> children = client.getChildren().usingWatcher(watcher).forPath(pathProvider);
            update(children, invokers);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void update(String pathProvider, List<Invoker> invokers) {
        try {
            invokers.clear();
            List<String> children = client.getChildren().forPath(pathProvider);
            update(children, invokers);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void update(List<String> children, List<Invoker> invokers) {
        for (String provider : children) {
            Invoker invoker = getInvoker(provider);
            if (invoker == null) {
                logger.error("invoker is null!");
            } else {
                invokers.add(invoker);
            }
        }
    }

    private static final Pattern PATTERN_DUBBO_URL = Pattern.compile("dubbo://([\\w.]*+):(\\w+)/([\\w.]+)");

    private static final Pattern PATTERN_RIM_URL = Pattern.compile("rmi://([\\w.]*+):(\\w+)/([\\w.]+)");

    private static final Pattern PATTERN_HTTP_URL = Pattern.compile("http://([\\w.]*+):(\\w+)/([\\w.]+)");

    private Invoker getInvoker(String provider) {
        try {
            // 1. 根据provider选择protocol，现在都是DubboProtocol
            String url = URLDecoder.decode(provider, "UTF-8");

            Matcher m = PATTERN_DUBBO_URL.matcher(url);
            if (m.find()) {
                String protocolIp = m.group(1);
                String protocolPort = m.group(2);
                String serviceName = m.group(3);

                return new DubboProtocol(protocolIp, Integer.parseInt(protocolPort)).refer(serviceName);
            }

            m = PATTERN_RIM_URL.matcher(url);
            if (m.find()) {
                String protocolIp = m.group(1);
                String protocolPort = m.group(2);
                String serviceName = m.group(3);
                return new RmiProtocol(protocolIp, Integer.parseInt(protocolPort)).refer(serviceName);
            }

            m = PATTERN_HTTP_URL.matcher(url);
            if (m.find()) {
                String protocolIp = m.group(1);
                String protocolPort = m.group(2);
                String serviceName = m.group(3);
                return new HttpProtocol(protocolIp, Integer.parseInt(protocolPort)).refer(serviceName);
            }
            logger.error("can not decode protocol");
            return null;
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
            return null;
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ZookeeperRegistry that = (ZookeeperRegistry) o;
        return port == that.port && Objects.equals(ip, that.ip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip, port);
    }
}
