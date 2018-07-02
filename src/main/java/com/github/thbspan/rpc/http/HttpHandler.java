package com.github.thbspan.rpc.http;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface HttpHandler {
    /**
     * invoke
     */
    void handle(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException;
}
