package com.github.thbspan.rpc.protocol;

import com.github.thbspan.rpc.http.HttpBinder;
import com.github.thbspan.rpc.http.HttpHandler;
import com.github.thbspan.rpc.http.HttpInvokerServiceExporter;
import com.github.thbspan.rpc.http.HttpServer;
import com.github.thbspan.rpc.http.tomcat.TomcatHttpBinder;
import com.github.thbspan.rpc.invoker.HttpInvoker;
import com.github.thbspan.rpc.invoker.Invoker;
import com.github.thbspan.rpc.provider.DubboExport;
import com.github.thbspan.rpc.provider.Export;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HttpProtocol extends Protocol {

    private final Map<String, HttpServer> serverMap = new ConcurrentHashMap<>();

    private final Map<String, HttpInvokerServiceExporter> skeletonMap = new ConcurrentHashMap<>();

    private HttpBinder httpBinder;

    public HttpProtocol(String ip, int port) {
        super(ip, port);
        // init httpBinder, 调用bind才会启动http服务
        httpBinder = new TomcatHttpBinder();
    }

    @Override
    public void export(Invoker invoker, Object target) {
        serverMap.computeIfAbsent(getIp() + ":" + getPort(),
                serverKey -> httpBinder.bind(getIp(), getPort(), new InternalHandler()));

        Export export = new DubboExport(invoker);
        super.setExport(invoker.getInterfaceClass().getName(), export);

        final HttpInvokerServiceExporter httpInvokerServiceExporter = new HttpInvokerServiceExporter();
        httpInvokerServiceExporter.setServiceInterface(invoker.getInterfaceClass());
        httpInvokerServiceExporter.setService(target);
        try {
            httpInvokerServiceExporter.afterPropertiesSet();
        } catch (Exception e){
            throw new IllegalStateException(e);
        }
        skeletonMap.put(invoker.getInterfaceClass().getName(), httpInvokerServiceExporter);
    }

    @Override
    public Invoker refer(String serviceName) {
        Invoker invoker = super.getRefers(serviceName);
        if (invoker != null) {
            return invoker;
        }

        invoker = new HttpInvoker(getServiceUrl(serviceName), serviceName);
        super.setRefers(serviceName, invoker);
        return invoker;
    }

    @Override
    public String getProtocolName() {
        return "http";
    }

    @Override
    public String getPathProvider(String serviceName) {
        return "/http/" + serviceName;
    }

    private class InternalHandler implements HttpHandler {

        static final String DEFAULT_METHOD = "POST";

        @Override
        public void handle(HttpServletRequest request, HttpServletResponse response)
                throws IOException, ServletException {
            String uri = request.getRequestURI();

            // 去掉/com.github.thbspan.rpc.service.IEcho 前面的斜线
            HttpInvokerServiceExporter skeleton = skeletonMap.get(uri.substring(1));

            if (!DEFAULT_METHOD.equalsIgnoreCase(request.getMethod())) {
                response.setStatus(500);
            } else {
                try {
                    skeleton.handleRequest(request, response);
                } catch (Throwable e) {
                    throw new ServletException(e);
                }
            }
        }
    }
}
