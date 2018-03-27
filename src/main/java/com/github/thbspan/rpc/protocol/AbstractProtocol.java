package com.github.thbspan.rpc.protocol;

import com.github.thbspan.rpc.common.logger.Logger;
import com.github.thbspan.rpc.common.logger.LoggerFactory;
import com.github.thbspan.rpc.common.utils.ConcurrentHashSet;
import com.github.thbspan.rpc.invoker.Invoker;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public abstract class AbstractProtocol implements Protocol{
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected final ConcurrentMap<String, Exporter<?>> exporterMap = new ConcurrentHashMap<>();

    protected final Set<Invoker<?>> invokers = new ConcurrentHashSet<>();


}
