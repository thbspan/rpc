package com.github.thbspan.rpc.transport;

import com.github.thbspan.rpc.invoker.Invocation;
import com.github.thbspan.rpc.invoker.Result;

public interface Client extends AutoCloseable{
    Result send(Invocation invocation);
}
