package com.github.thbspan.rpc.invoker;

import com.github.thbspan.rpc.common.logger.Logger;
import com.github.thbspan.rpc.common.logger.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.Remote;

public class RmiInvoker implements Invoker {
    private static final Logger LOGGER = LoggerFactory.getLogger(RmiInvoker.class);
    private Remote stub;
    private String serviceName;

    public RmiInvoker(Remote stub, String serviceName) {
        this.stub = stub;
        this.serviceName = serviceName;
    }

    @Override
    public Class getInterfaceClass() {
        try {
            return Class.forName(serviceName);
        } catch (ClassNotFoundException e) {
            LOGGER.error(e);
        }
        return null;
    }

    @Override
    public Result doInvoker(Invocation invocation) {
        Result result = new Result();

        try {
            Method method = invocation.getInterfaceClass().getMethod(invocation.getMethodName(), invocation.getParameterTypes());
            if (method.getDeclaringClass().isInstance(stub)) {
                // directly implemented
                result.setValue(method.invoke(stub, invocation.getArgs()));
            } else {
                // not directly implemented
                result.setValue(stub.getClass().getMethod(method.getName(), method.getParameterTypes())
                        .invoke(stub, invocation.getArgs()));
            }
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException ex) {
            result.setException(ex);
        }

        return result;
    }
}
