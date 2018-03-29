package com.github.thbspan.rpc.consumer;

import com.github.thbspan.rpc.invoker.AllInOneInvoker;
import com.github.thbspan.rpc.invoker.Invoker;
import com.github.thbspan.rpc.registry.Registry;

import java.lang.reflect.Proxy;
import java.util.*;

public class Consumer {
    private Set<Registry> registrys = new LinkedHashSet<>();
    private Map<Class<?>,Object> refers = new HashMap<>();

    /**
     * 根据clazz在注册中心查找服务，并生成Invoker，包装为clazz的实现类返回
     */
    public Object refer(Class<?> clazz) {
        return refers.computeIfAbsent(clazz, keyClazz -> {
            List<Invoker> invokers = new ArrayList<>();
            //1. 在注册中心查找
            for (Registry registry : registrys) {
                registry.subscribe(clazz.getName(), invokers);
            }
            // 2. 合成一个invoker
            Invoker allInOne = new AllInOneInvoker(invokers);
            // 3. 生成一个代理类
            return Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new ConsumerInvokeHandler(allInOne));
        });
    }

    public void addRegistry(Registry registry) {
        registrys.add(registry);
    }
}
