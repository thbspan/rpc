package com.github.thbspan.rpc.invoker;

import java.util.List;

public class AllInOneInvoker implements Invoker{
    private List<Invoker> list;
    public AllInOneInvoker(List<Invoker> list) {
        this.list = list;
    }

    @Override
    public Class getInterfaceClass() {
        return list.get(0).getInterfaceClass();
    }

    @Override
    public Result doInvoker(Invocation invocation) {
        return list.get(0).doInvoker(invocation);
    }
}
