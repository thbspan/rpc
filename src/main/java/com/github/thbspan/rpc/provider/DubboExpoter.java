package com.github.thbspan.rpc.provider;

import com.github.thbspan.rpc.invoker.Invoker;

public class DubboExpoter extends Export {

    public DubboExpoter(Invoker invoker) {
        super(invoker);
    }
}
