package com.github.thbspan.rpc.protocol;

import com.github.thbspan.rpc.invoker.Invoker;

public interface Exporter<T> {

    Invoker<T> getInvoker();

    void unexport();
}
