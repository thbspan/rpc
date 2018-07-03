package com.github.thbspan.rpc.http.tomcat;

import com.github.thbspan.rpc.http.HttpBinder;
import com.github.thbspan.rpc.http.HttpHandler;
import com.github.thbspan.rpc.http.HttpServer;


public class TomcatHttpBinder implements HttpBinder {
    @Override
    public HttpServer bind(String ip, int port, HttpHandler handler) {
        return new TomcatHttpServer(ip, port, handler);
    }
}
