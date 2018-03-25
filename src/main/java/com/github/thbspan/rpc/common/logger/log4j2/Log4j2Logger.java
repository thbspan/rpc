package com.github.thbspan.rpc.common.logger.log4j2;

import com.github.thbspan.rpc.common.logger.Logger;
import org.apache.log4j.Level;

public class Log4j2Logger implements Logger{
    private org.apache.log4j.Logger logger;

    public Log4j2Logger(org.apache.log4j.Logger logger){
        this.logger = logger;
    }

    public Log4j2Logger(Class<?> clazz){
        this.logger = org.apache.log4j.Logger.getLogger(clazz);
    }
    @Override
    public void trace(String msg) {
        logger.trace(msg);
    }

    @Override
    public void trace(Throwable e) {
        logger.trace(e);
    }

    @Override
    public void trace(String msg, Throwable e) {
        logger.trace(msg, e);
    }

    @Override
    public void debug(String msg) {
        logger.debug(msg);
    }

    @Override
    public void debug(String msg, Throwable e) {
        logger.debug(msg, e);
    }

    @Override
    public void info(String msg) {
        logger.info(msg);
    }

    @Override
    public void info(Throwable e) {
        logger.info(e);
    }

    @Override
    public void info(String msg, Throwable e) {
        logger.info(msg, e);
    }

    @Override
    public void warn(String msg) {
        logger.warn(msg);
    }

    @Override
    public void warn(Throwable e) {
        logger.warn(e);
    }

    @Override
    public void warn(String msg, Throwable e) {
        logger.warn(msg, e);
    }

    @Override
    public void error(String msg) {
        logger.error(msg);
    }

    @Override
    public void error(Throwable e) {
        logger.error(e);
    }

    @Override
    public void error(String msg, Throwable e) {
        logger.error(msg, e);
    }

    @Override
    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    @Override
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    @Override
    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    @Override
    public boolean isWarnEnabled() {
        return logger.isEnabledFor(Level.WARN);
    }

    @Override
    public boolean isErrorEnabled() {
        return logger.isEnabledFor(Level.ERROR);
    }
}
