package com.github.thbspan.rpc.invoker;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class InvokerUtil {

    public static Result invokeMethod(Object target, Invocation invocation){
        Result result = new Result();

        try {
            Method method = invocation.getInterfaceClass().getMethod(invocation.getMethodName(), invocation.getParameterTypes());
            if (method.getDeclaringClass().isInstance(target)) {
                // directly implemented
                result.setValue(method.invoke(target, invocation.getArgs()));
            } else {
                // not directly implemented
                result.setValue(target.getClass().getMethod(method.getName(), method.getParameterTypes())
                        .invoke(target, invocation.getArgs()));
            }
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException ex) {
            result.setException(ex);
        }
        return result;
    }

    private InvokerUtil(){}
}
