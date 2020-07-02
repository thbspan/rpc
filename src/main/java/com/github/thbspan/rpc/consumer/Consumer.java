package com.github.thbspan.rpc.consumer;

import com.github.thbspan.rpc.invoker.AllInOneInvoker;
import com.github.thbspan.rpc.invoker.Invoker;
import com.github.thbspan.rpc.protocol.Protocol;
import com.github.thbspan.rpc.registry.Registry;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Consumer {
    private final Set<Registry> registries = new LinkedHashSet<>();
    private Protocol protocol;
    private final Map<Class<?>, Object> refers = new ConcurrentHashMap<>();

    /**
     * 根据clazz在注册中心查找服务，并生成Invoker，包装为clazz的实现类返回
     */
    public Object refer(Class<?> clazz) {
        return refers.computeIfAbsent(clazz, keyClazz -> {
            List<Invoker> invokers = new ArrayList<>();
            //1. 在注册中心查找
            for (Registry registry : registries) {
                registry.subscribe(getProtocol(), clazz.getName(), invokers);
            }
            // 2. 合成一个invoker
            Invoker allInOne = new AllInOneInvoker(invokers);
            // 3. 生成一个代理类
            return Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new ConsumerInvokeHandler(allInOne));
        });
    }

    public void addRegistry(Registry registry) {
        registries.add(registry);
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }
}
