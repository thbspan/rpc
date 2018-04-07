package com.github.thbspan.rpc.service;

public class EchoImpl implements IEcho {
    @Override
    public String echo() {
        return "hello";
    }
}
