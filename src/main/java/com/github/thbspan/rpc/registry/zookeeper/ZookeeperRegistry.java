package com.github.thbspan.rpc.registry.zookeeper;

import com.github.thbspan.rpc.common.logger.Logger;
import com.github.thbspan.rpc.common.logger.LoggerFactory;
import com.github.thbspan.rpc.invoker.Invoker;
import com.github.thbspan.rpc.protocol.Protocol;
import com.github.thbspan.rpc.registry.Registry;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.List;

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
                .connectString(ip+":"+port)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .connectionTimeoutMs(5000);

        client = builder.build();
        client.start();
    }

    @Override
    public void registry(Protocol protocol, Invoker invoker) {
        String serviceName = invoker.getInterfaceClass().getName();

        client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL);
    }

    @Override
    public void subscribe(String serviceName, List<Invoker> invokers) {

    }
}
