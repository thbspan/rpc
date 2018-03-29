package com.github.thbspan.rpc.protocol;

import com.github.thbspan.rpc.invoker.Invoker;
import com.github.thbspan.rpc.provider.Export;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public abstract class Protocol extends ProtocolPointEnd{

    private ConcurrentMap<String, Export> exports = new ConcurrentHashMap<>();
    private ConcurrentMap<String, Invoker> refers = new ConcurrentHashMap<>();

    public Protocol(String ip, int port) {
        super(ip, port);
    }

    public abstract void export(Invoker invoker);

    public abstract Invoker refer(String serviceName);

    public abstract String getPortocolName();

    public void setExport(String serviceName, Export export) {
        exports.put(serviceName, export);
    }

    public Export getExport(String serviceName) {
        return exports.get(serviceName);
    }

    public String getServiceUrl(String serviceName) {
        return String.format("%s://%s:%d/%s", getPortocolName(), getIp(), getPort(), serviceName);
    }

    public void setRefers(String serviceName, Invoker export) {
        refers.put(serviceName, export);
    }

    public Invoker getRefers(String serviceName) {
        return refers.get(serviceName);
    }
}
