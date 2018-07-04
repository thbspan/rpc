package com.github.thbspan.rpc.http.tomcat;

import com.github.thbspan.rpc.http.HttpHandler;
import com.github.thbspan.rpc.http.HttpServer;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Paths;


public class TomcatHttpServer implements HttpServer {

    private String ip;
    private int port;
    private final Tomcat tomcat;
    public TomcatHttpServer(String ip, int port, HttpHandler handler) {
        this.ip = ip;
        this.port = port;
        tomcat = new Tomcat();
        tomcat.setHostname(ip);
        tomcat.setPort(port);
        tomcat.getHost().setAutoDeploy(false);
        tomcat.setBaseDir(Paths.get(System.getProperty("java.io.tmpdir"), "tomcat").toString());

        String contextPath = "";
        StandardContext context = new StandardContext();
        context.setPath(contextPath);
        context.addLifecycleListener(new Tomcat.FixContextListener());
        tomcat.getHost().addChild(context);

        tomcat.addServlet(contextPath, "httpServer",  new HttpServlet(){
            @Override
            protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                handler.handle(req, resp);
            }
        });

        context.addServletMappingDecoded("/", "httpServer");
        try {
            tomcat.start();
        } catch (LifecycleException e) {
            throw new IllegalStateException("failed to start tomcat on ip="+ip + ", port=" +port
                    + " cause:"+e.getMessage(), e);
        }
    }

    @Override
    public void close() {
        try {
            tomcat.stop();
        } catch (LifecycleException e) {
            throw new IllegalStateException("failed to start tomcat on ip="+ip + ", port=" +port
                    + " cause:"+e.getMessage(), e);
        }
    }
}
