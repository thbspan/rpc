package com.github.thbspan.rpc.invoker;

import java.lang.reflect.Method;

public class ProviderInvoker implements Invoker {
    private final Object target;
    private final Class<?> interfaceClass;

    public ProviderInvoker(Class<?> clazz, Object target) {
        this.interfaceClass = clazz;
        this.target = target;
    }

    @Override
    public Class<?> getInterfaceClass() {
        return interfaceClass;
    }

    @Override
    public Result doInvoker(Invocation invocation) {
        Result result = new Result();
        try {
            Method method = invocation.getInterfaceClass().getMethod(invocation.getMethodName(), invocation.getParameterTypes());
            result.setValue(method.invoke(target, invocation.getArgs()));
        } catch (Exception e) {
            result.setException(e);
        }
        return result;
    }
}
