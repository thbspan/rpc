package com.github.thbspan.rpc.invoker;

public interface Invoker {
    Class<?> getInterfaceClass();

    Result doInvoker(Invocation invocation);
}
