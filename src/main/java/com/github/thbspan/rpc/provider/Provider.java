package com.github.thbspan.rpc.provider;

import com.github.thbspan.rpc.invoker.Invoker;
import com.github.thbspan.rpc.invoker.ProviderInvoker;
import com.github.thbspan.rpc.protocol.Protocol;
import com.github.thbspan.rpc.registry.Registry;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class Provider {
    private Set<Registry> registrys = new LinkedHashSet<>();
    private Set<Protocol> protocols = new LinkedHashSet<>();
    private static Map<String, Invoker> invokers = new HashMap<>();

    public void export(Class<?> clazz, Object target) {
        // 1. 创建invoker
        Invoker invoker = invokers.computeIfAbsent(clazz.getName(), key -> getInvoker(clazz, target));
        // 2. 发布到协议
        for (Protocol protocol : protocols) {
            export(protocol, invoker);
        }
    }

    private void export(Protocol protocol, Invoker invoker) {
        Export export = protocol.getExport(invoker.getInterfaceClass().getName());
        if (export != null) {
            return;
        }
        // 1. 注册到注册中心
        for (Registry registry : registrys) {
            registry.registry(protocol, invoker);
        }
        // 2. 发布到协议中
        protocol.export(invoker);
    }

    public void addRegistry(Registry registry) {
        registrys.add(registry);
    }

    public void addProtocol(Protocol protocol) {
        protocols.add(protocol);
    }

    private Invoker getInvoker(Class<?> clazz, Object target) {
        return new ProviderInvoker(clazz, target);
    }
}
