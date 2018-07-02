package com.github.thbspan.rpc.http;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DispatcherServlet extends HttpServlet {
    private static final Map<Integer, HttpHandler> handlers = new ConcurrentHashMap<>();

    private static DispatcherServlet INSTANCE;

    public DispatcherServlet() {
        DispatcherServlet.INSTANCE = this;
    }

    public static void addHttpHandler(int port, HttpHandler processor) {
        handlers.put(port, processor);
    }

    public static DispatcherServlet getInstance() {
        return INSTANCE;
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpHandler handler = handlers.get(request.getLocalPort());
        // service not found.
        if (handler == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Service not found.");
        } else {
            handler.handle(request, response);
        }
    }
}
