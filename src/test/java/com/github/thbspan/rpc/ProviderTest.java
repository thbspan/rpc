package com.github.thbspan.rpc;

import java.rmi.RemoteException;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import com.github.thbspan.rpc.consumer.Consumer;
import com.github.thbspan.rpc.protocol.DubboProtocol;
import com.github.thbspan.rpc.protocol.HttpProtocol;
import com.github.thbspan.rpc.protocol.Protocol;
import com.github.thbspan.rpc.protocol.RmiProtocol;
import com.github.thbspan.rpc.provider.Provider;
import com.github.thbspan.rpc.registry.Registry;
import com.github.thbspan.rpc.registry.zookeeper.ZookeeperRegistry;
import com.github.thbspan.rpc.service.EchoImpl;
import com.github.thbspan.rpc.service.IEcho;
import com.github.thbspan.rpc.service.IRemote;
import com.github.thbspan.rpc.service.IRemoteImpl;
import com.github.thbspan.rpc.service.ISearchPrice;
import com.github.thbspan.rpc.service.SearchPriceImpl;

public class ProviderTest {

    @Test
    public void testDubbo() {
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
        IEcho echo = (IEcho) consumer.refer(IEcho.class);
        System.out.println(echo);
        System.out.println(echo.echo());
    }

    public static void main(String[] args) {
        Provider provider = new Provider();

        Registry registry = new ZookeeperRegistry("127.0.0.1", 2181);
        provider.addRegistry(registry);
        Protocol protocol = new DubboProtocol("127.0.0.1", 3307);
        provider.addProtocol(protocol);
        provider.export(ISearchPrice.class, new SearchPriceImpl());

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println(scanner.nextLine());
        }
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

    @Test
    public void testHttp() {
        Provider provider = new Provider();

        Registry registry = new ZookeeperRegistry("127.0.0.1", 2181);
        provider.addRegistry(registry);

        Protocol protocol = new HttpProtocol("127.0.0.1", 9090);

        provider.addProtocol(protocol);

        provider.export(IEcho.class, new EchoImpl());

        Consumer consumer = new Consumer();
        consumer.addRegistry(registry);

        consumer.setProtocol(protocol);
        IEcho echo = (IEcho) consumer.refer(IEcho.class);
        System.out.println(echo.echo());
    }
}
