package com.github.thbspan.rpc.consumer;

import com.github.thbspan.rpc.invoker.Invocation;
import com.github.thbspan.rpc.invoker.Invoker;
import com.github.thbspan.rpc.invoker.Result;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ConsumerInvokeHandler implements InvocationHandler {
    private final Invoker invoker;

    public ConsumerInvokeHandler(Invoker invoker) {
        this.invoker = invoker;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getDeclaringClass().equals(Object.class)) {
            return method.invoke(invoker, args);
        } else {
            Result result = invoker.doInvoker(new Invocation(invoker.getInterfaceClass(), method.getName(), method.getParameterTypes(), args));
            if (result.getException() != null){
                throw new RuntimeException(result.getException());
            }
            return result.getValue();
        }
    }
}
