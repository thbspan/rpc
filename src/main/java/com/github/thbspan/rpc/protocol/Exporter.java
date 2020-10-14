package com.github.thbspan.rpc.protocol;

import com.github.thbspan.rpc.invoker.Invoker;

public interface Exporter {

    Invoker getInvoker();

    void unexport();
}
