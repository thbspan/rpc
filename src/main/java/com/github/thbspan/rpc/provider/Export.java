package com.github.thbspan.rpc.provider;

import com.github.thbspan.rpc.invoker.Invoker;

public class Export {
    private Invoker invoker;

    public Export(Invoker invoker) {
        this.invoker = invoker;
    }

    public Invoker getInvoker() {
        return this.invoker;
    }
}
