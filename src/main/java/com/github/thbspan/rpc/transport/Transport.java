package com.github.thbspan.rpc.transport;

public interface Transport {
    Server bind(String ip,int port);

    Client connect(String ip,int port);
}
