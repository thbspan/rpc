package com.github.thbspan.rpc;

import com.github.thbspan.rpc.consumer.Consumer;
import com.github.thbspan.rpc.protocol.DubboProtocol;
import com.github.thbspan.rpc.protocol.Protocol;
import com.github.thbspan.rpc.protocol.RmiProtocol;
import com.github.thbspan.rpc.provider.Provider;
import com.github.thbspan.rpc.registry.Registry;
import com.github.thbspan.rpc.registry.zookeeper.ZookeeperRegistry;
import com.github.thbspan.rpc.service.*;
import org.junit.Test;

import java.rmi.RemoteException;

public class ProviderTest {

    @Test
    public void testDubbo(){
        Provider provider = new Provider();

        Registry registry = new ZookeeperRegistry("127.0.0.1", 2181);
        provider.addRegistry(registry);
        Protocol protocol = new DubboProtocol("127.0.0.1", 3307);
        provider.addProtocol(protocol);

        provider.export(ISearchPrice.class, new SearchPriceImpl());

        provider.export(IEcho.class, new EchoImpl());
        Consumer consumer = new Consumer();
        consumer.addRegistry(registry);
        consumer.setProtocol(protocol);
        ISearchPrice searcher = (ISearchPrice) consumer.refer(ISearchPrice.class);
        String rs = searcher.getPrice("Python First Head");
        System.out.println("rs is " + rs);
        IEcho echo = null;
//        IEcho echo = (IEcho) consumer.refer(IEcho.class);
        System.out.println(echo);
//        System.out.println(echo.echo());
    }

    @Test
    public void testRmi() throws RemoteException {
        Provider provider = new Provider();

        Registry registry = new ZookeeperRegistry("127.0.0.1", 2181);
        provider.addRegistry(registry);

        Protocol protocol = new RmiProtocol("127.0.0.1", 3308);
        provider.addProtocol(protocol);
        provider.export(IRemote.class, new IRemoteImpl());
        Consumer consumer = new Consumer();
        consumer.addRegistry(registry);
        consumer.setProtocol(protocol);
        IRemote iRemote = (IRemote) consumer.refer(IRemote.class);
        System.out.print(iRemote.echo());
    }

}
