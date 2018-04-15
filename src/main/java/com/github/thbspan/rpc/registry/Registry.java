package com.github.thbspan.rpc.registry;

import com.github.thbspan.rpc.invoker.Invoker;
import com.github.thbspan.rpc.protocol.Protocol;

import java.util.List;

public interface Registry {
    void registry(Protocol protocol, Invoker invoker);
    void subscribe(Protocol protocol, String serviceName,List<Invoker> invokers);
}
