package com.github.thbspan.rpc.protocol;

import com.github.thbspan.rpc.invoker.DubboInvoker;
import com.github.thbspan.rpc.invoker.Invoker;
import com.github.thbspan.rpc.provider.DubboExport;
import com.github.thbspan.rpc.provider.Export;
import com.github.thbspan.rpc.transport.Client;
import com.github.thbspan.rpc.transport.NettyTransport;
import com.github.thbspan.rpc.transport.Server;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DubboProtocol extends Protocol {
    private static ConcurrentMap<String, Server> servers = new ConcurrentHashMap<>();
    private static ConcurrentMap<String, Client> clients = new ConcurrentHashMap<>();

    public DubboProtocol(String ip, int port) {
        super(ip, port);
    }

    @Override
    public String getProtocolName() {
        return "dubbo";
    }

    @Override
    public String getPathProvider(String serviceName) {
        return "/dubbo/" + serviceName + "/providers";
    }

    @Override
    public void export(Invoker invoker, Object target) {
        Export export = new DubboExport(invoker);
        super.setExport(invoker.getInterfaceClass().getName(), export);
        openServer();
    }

    private void openServer() {
        String serverKey = getIp() + ":" + getPort();

        servers.computeIfAbsent(serverKey,
                key -> new NettyTransport().bind(getIp(), getPort()));
    }

    @Override
    public Invoker refer(String serviceName) {
        Invoker invoker = super.getRefers(serviceName);
        if (invoker != null) {
            return invoker;
        }
        Client client = openClient();
        invoker = new DubboInvoker(client, serviceName);
        super.setRefers(serviceName, invoker);
        return invoker;
    }

    private Client openClient() {
        return clients.computeIfAbsent(getIp() + getPort(),
                key -> new NettyTransport().connect(getIp(), getPort()));
    }
}
