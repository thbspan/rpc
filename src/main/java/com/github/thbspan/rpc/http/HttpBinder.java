package com.github.thbspan.rpc.http;

public interface HttpBinder {
    HttpServer bind(String ip, int port, HttpHandler handler);
}
