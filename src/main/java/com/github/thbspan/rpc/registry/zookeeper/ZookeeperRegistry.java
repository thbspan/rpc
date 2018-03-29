package com.github.thbspan.rpc.registry.zookeeper;

import com.github.thbspan.rpc.common.logger.Logger;
import com.github.thbspan.rpc.common.logger.LoggerFactory;
import com.github.thbspan.rpc.invoker.Invoker;
import com.github.thbspan.rpc.protocol.DubboProtocol;
import com.github.thbspan.rpc.protocol.Protocol;
import com.github.thbspan.rpc.registry.Registry;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ZookeeperRegistry implements Registry {
    private static Logger logger = LoggerFactory.getLogger(ZookeeperRegistry.class);

    private String ip;
    private int port;
    private CuratorFramework client;

    public ZookeeperRegistry(String ip, int port) {
        this.ip = ip;
        this.port = port;
        init();
    }

    private void init() {
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
                .connectString(ip + ":" + port)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .connectionTimeoutMs(5000);

        client = builder.build();
        client.start();
    }

    @Override
    public void registry(Protocol protocol, Invoker invoker) {
        String serviceName = invoker.getInterfaceClass().getName();
        String url = protocol.getServiceUrl(serviceName);
        try {
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(getPath(url, serviceName));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private String getPath(String url, String serviceName) {
        try {
            return getPathProvider(serviceName) + "/" + URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    private String getPathProvider(String serviceName) {
        return "/dubbo/" + serviceName + "/providers";
    }

    @Override
    public void subscribe(String serviceName, List<Invoker> invokers) {
        try {
            Watcher watcher = event -> update(serviceName, invokers);

            client.getChildren().usingWatcher(watcher).forPath(getPathProvider(serviceName));
        } catch (Exception e) {
            logger.error(e);
        }
        update(serviceName, invokers);
    }

    private void update(String serviceName, List<Invoker> invokers) {
        try {
            invokers.clear();
            List<String> children = client.getChildren().forPath(getPathProvider(serviceName));
            for (String provider : children) {
                Invoker invoker = getInvoker(provider);
                if (invoker == null) {
                    logger.error("invoker is null!");
                } else {
                    invokers.add(invoker);
                }
            }
        } catch (Exception e) {
            logger.error(e);
        }
    }

    private static final Pattern PATTERN_DUBBO_URL = Pattern.compile("dubbo://([\\w.]*+):(\\w+)/([\\w.]+)");

    private Invoker getInvoker(String provider) {
        try {
            // 1. 根据provider选择protocol，现在都是DubboProtocol
            Invoker invoker;
            String url = URLDecoder.decode(provider, "UTF-8");

            Matcher m = PATTERN_DUBBO_URL.matcher(url);
            if (m.find()) {
                String protocolIp = m.group(1);
                String protocolPort = m.group(2);
                String serviceName = m.group(3);
                Protocol protocol = new DubboProtocol(protocolIp, Integer.valueOf(protocolPort));
                invoker = protocol.refer(serviceName);
            } else {
                logger.error("can not decode protocol");
                return null;
            }
            return invoker;
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
            return null;
        }

    }
}
