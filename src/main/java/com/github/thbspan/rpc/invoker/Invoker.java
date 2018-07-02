package com.github.thbspan.rpc.invoker;

public interface Invoker<T> {
    Class<T> getInterfaceClass();

    Result doInvoker(Invocation invocation);
}
