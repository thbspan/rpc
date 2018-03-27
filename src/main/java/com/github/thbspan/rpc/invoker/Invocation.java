package com.github.thbspan.rpc.invoker;

import java.io.Serializable;

public class Invocation implements Serializable{
    private static final long serialVersionUID = -7850874120116176330L;
    private Class<?> interfaceClass;
    private String methodName;
    private Class<?>[] argTypes;
    private Object[] args;

    public Invocation(Class<?> interfaceClass, String methodName, Class<?>[] argTypes, Object[] args) {
        this.interfaceClass = interfaceClass;
        this.methodName = methodName;
        this.argTypes = argTypes;
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

    public Class<?>[] getArgTypes() {
        return argTypes;
    }

    public void setArgTypes(Class<?>[] argTypes) {
        this.argTypes = argTypes;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }
}
