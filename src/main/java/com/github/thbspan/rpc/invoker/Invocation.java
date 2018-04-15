package com.github.thbspan.rpc.invoker;

import java.io.Serializable;

public class Invocation implements Serializable{
    private static final long serialVersionUID = -7850874120116176330L;

    private Class<?> interfaceClass;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] args;

    public Invocation(Class<?> interfaceClass, String methodName, Class<?>[] parameterTypes, Object[] args) {
        this.interfaceClass = interfaceClass;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.args = args;
    }
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Class<?> getInterfaceClass() {
        return interfaceClass;
    }

    public void setInterfaceClass(Class<?> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }
}
