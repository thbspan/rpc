package com.github.thbspan.rpc.protocol;

import com.github.thbspan.rpc.invoker.Invoker;
import com.github.thbspan.rpc.provider.Export;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public abstract class Protocol extends ProtocolPointEnd{

    private final ConcurrentMap<String, Export> exports = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Invoker> refers = new ConcurrentHashMap<>();

    public Protocol(String ip, int port) {
        super(ip, port);
    }

    public abstract void export(Invoker invoker, Object target);

    public abstract Invoker refer(String serviceName);

    public abstract String getProtocolName();

    public void setExport(String serviceName, Export export) {
        exports.put(serviceName, export);
    }

    public Export getExport(String serviceName) {
        return exports.get(serviceName);
    }

    public String getServiceUrl(String serviceName) {
        return String.format("%s://%s:%d/%s", getProtocolName(), getIp(), getPort(), serviceName);
    }

    public abstract String getPathProvider(String serviceName);

    public void setRefers(String serviceName, Invoker export) {
        refers.put(serviceName, export);
    }

    public Invoker getRefers(String serviceName) {
        return refers.get(serviceName);
    }
}
