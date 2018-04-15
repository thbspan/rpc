package com.github.thbspan.rpc.invoker;

import java.lang.reflect.Method;

public class ProviderInvoker<T> implements Invoker<T> {
    private Object target;
    private Class<T> interfaceClass;

    public ProviderInvoker(Class<T> clazz, Object target) {
        this.interfaceClass = clazz;
        this.target = target;
    }

    @Override
    public Class<T> getInterfaceClass() {
        return interfaceClass;
    }

    @Override
    public Result doInvoker(Invocation invocation) {
        Result result = new Result();
        try {
            Method method = invocation.getInterfaceClass().getMethod(invocation.getMethodName(), invocation.getParameterTypes());
            if (method != null) {
                result.setValue(method.invoke(target, invocation.getArgs()));
            }
        } catch (Exception e) {
            result.setException(e);
        }
        return result;
    }
}
