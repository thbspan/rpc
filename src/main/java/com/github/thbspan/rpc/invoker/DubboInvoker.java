package com.github.thbspan.rpc.invoker;

import com.github.thbspan.rpc.common.logger.Logger;
import com.github.thbspan.rpc.common.logger.LoggerFactory;
import com.github.thbspan.rpc.transport.Client;

public class DubboInvoker implements Invoker{
    private static final Logger LOGGER = LoggerFactory.getLogger(DubboInvoker.class);
    private Client client;
    private String serviceName;

    public DubboInvoker(Client client,String serviceName) {
        this.client = client;
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
        return client.send(invocation);
    }
}
