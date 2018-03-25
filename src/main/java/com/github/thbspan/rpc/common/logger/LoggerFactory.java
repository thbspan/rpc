package com.github.thbspan.rpc.common.logger;

import com.github.thbspan.rpc.common.logger.log4j2.Log4j2Logger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class LoggerFactory {

    /**
     * 缓存className-logger
     */
    private static final ConcurrentMap<Class<?>, Logger> LOGGERS = new ConcurrentHashMap<>();
    private LoggerFactory(){
    }

    public static Logger getLogger(Class<?> key) {
        return LOGGERS.computeIfAbsent(key, Log4j2Logger::new) ;
    }
}
