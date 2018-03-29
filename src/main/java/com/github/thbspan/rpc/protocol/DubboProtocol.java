package com.github.thbspan.rpc.protocol;

import com.github.thbspan.rpc.invoker.Invoker;
import com.github.thbspan.rpc.transport.Client;
import com.github.thbspan.rpc.transport.Server;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DubboProtocol extends Protocol {
    private  static ConcurrentMap<String, Server> servers = new ConcurrentHashMap<>();
    private  static ConcurrentMap<String, Client> clients = new ConcurrentHashMap<>();
    public DubboProtocol(String ip, int port) {
        super(ip, port);
    }

    @Override
    public void export(Invoker invoker) {

    }

    @Override
    public Invoker refer(String serviceName) {
        return null;
    }

    @Override
    public String getPortocolName() {
        return null;
    }
}
