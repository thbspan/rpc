package com.github.thbspan.rpc.provider;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.github.thbspan.rpc.invoker.Invoker;
import com.github.thbspan.rpc.invoker.ProviderInvoker;
import com.github.thbspan.rpc.protocol.Protocol;
import com.github.thbspan.rpc.registry.Registry;

public class Provider {
    public static final Map<String, Invoker> INVOKERS = new HashMap<>();
    private final Set<Registry> registries = new LinkedHashSet<>();
    private final Set<Protocol> protocols = new LinkedHashSet<>();

    public void export(Class<?> interfaceClass, Object target) {
        // 1. 创建invoker
        Invoker invoker = INVOKERS.computeIfAbsent(interfaceClass.getName(), __ -> getInvoker(interfaceClass, target));
        // 2. 发布到协议
        for (Protocol protocol : protocols) {
            export(protocol, invoker, target);
        }
    }

    private void export(Protocol protocol, Invoker invoker, Object target) {
        Export export = protocol.getExport(invoker.getInterfaceClass().getName());
        if (export != null) {
            return;
        }
        // 1. 发布到协议中
        protocol.export(invoker, target);
        // 2. 注册到注册中心
        for (Registry registry : registries) {
            registry.registry(protocol, invoker);
        }
    }

    public void addRegistry(Registry registry) {
        registries.add(registry);
    }

    public void addProtocol(Protocol protocol) {
        protocols.add(protocol);
    }

    private Invoker getInvoker(Class<?> interfaceClass, Object target) {
        return new ProviderInvoker(interfaceClass, target);
    }
}
