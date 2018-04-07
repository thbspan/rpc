package com.github.thbspan.rpc;

import com.github.thbspan.rpc.consumer.Consumer;
import com.github.thbspan.rpc.protocol.DubboProtocol;
import com.github.thbspan.rpc.provider.Provider;
import com.github.thbspan.rpc.registry.Registry;
import com.github.thbspan.rpc.registry.zookeeper.ZookeeperRegistry;
import com.github.thbspan.rpc.service.EchoImpl;
import com.github.thbspan.rpc.service.IEcho;
import com.github.thbspan.rpc.service.ISearchPrice;
import com.github.thbspan.rpc.service.SearchPriceImpl;
import org.junit.Test;

public class ProviderTest {

    @Test
    public void testProvider(){
        Provider provider = new Provider();

        Registry registry = new ZookeeperRegistry("127.0.0.1", 2181);
        provider.addRegistry(registry);

        provider.addProtocol(new DubboProtocol("127.0.0.1", 3307));

        provider.export(ISearchPrice.class, new SearchPriceImpl());

        provider.export(IEcho.class, new EchoImpl());
        Consumer consumer = new Consumer();
        consumer.addRegistry(registry);
        ISearchPrice searcher = (ISearchPrice) consumer.refer(ISearchPrice.class);
        String rs = searcher.getPrice("Python First Head");
        System.out.println("rs is " + rs);
//        IEcho echo = null;
        IEcho echo = (IEcho) consumer.refer(IEcho.class);
        System.out.println(echo);
        System.out.println(echo.echo());
    }
}
