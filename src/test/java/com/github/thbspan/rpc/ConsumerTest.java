package com.github.thbspan.rpc;

import com.github.thbspan.rpc.consumer.Consumer;
import com.github.thbspan.rpc.protocol.DubboProtocol;
import com.github.thbspan.rpc.protocol.Protocol;
import com.github.thbspan.rpc.registry.Registry;
import com.github.thbspan.rpc.registry.zookeeper.ZookeeperRegistry;
import com.github.thbspan.rpc.service.ISearchPrice;
import org.junit.Test;

public class ConsumerTest {

    @Test
    public void testConsumer(){
        Registry registry = new ZookeeperRegistry("127.0.0.1", 2181);

        Consumer consumer = new Consumer();
        consumer.addRegistry(registry);
        Protocol protocol = new DubboProtocol("127.0.0.1", 3307);
        consumer.setProtocol(protocol);
        ISearchPrice searcher = (ISearchPrice) consumer.refer(ISearchPrice.class);
        String rs = searcher.getPrice("Python First Head");
        System.out.println("rs is " + rs);
    }
}
