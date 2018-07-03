package com.github.thbspan.rpc.invoker;

import com.github.thbspan.rpc.common.logger.Logger;
import com.github.thbspan.rpc.common.logger.LoggerFactory;

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
        return InvokerUtil.invokeMethod(stub, invocation);
    }
}
