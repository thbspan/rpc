package com.github.thbspan.rpc.protocol;

import com.github.thbspan.rpc.common.URL;
import com.github.thbspan.rpc.invoker.Invoker;

public interface Protocol {
    <T> Exporter<T> export(Invoker<T> invoker);

    <T> Invoker<T> refer(Class<T> type, URL url);
}
