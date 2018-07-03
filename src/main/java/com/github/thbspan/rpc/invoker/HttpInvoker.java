package com.github.thbspan.rpc.invoker;

import com.github.thbspan.rpc.common.logger.Logger;
import com.github.thbspan.rpc.common.logger.LoggerFactory;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;
import org.springframework.remoting.httpinvoker.SimpleHttpInvokerRequestExecutor;

import java.io.IOException;
import java.net.HttpURLConnection;

public class HttpInvoker implements Invoker {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpInvoker.class);
    private final String serviceUrl;
    private final String serviceName;

    private Object target;
    public HttpInvoker(String serviceUrl, String serviceName) {
        this.serviceUrl = serviceUrl;
        this.serviceName = serviceName;

        init();
    }

    private void init(){
        final HttpInvokerProxyFactoryBean httpProxyFactoryBean = new HttpInvokerProxyFactoryBean();
        httpProxyFactoryBean.setServiceUrl(serviceUrl);
        httpProxyFactoryBean.setServiceInterface(getInterfaceClass());

        SimpleHttpInvokerRequestExecutor httpInvokerRequestExecutor = new SimpleHttpInvokerRequestExecutor() {

            @Override
            protected void prepareConnection(HttpURLConnection con,
                                             int contentLength) throws IOException {
                super.prepareConnection(con, contentLength);
                // 设置超时时间
                con.setReadTimeout(1000);
                con.setConnectTimeout(3000);
            }
        };
        httpProxyFactoryBean.setHttpInvokerRequestExecutor(httpInvokerRequestExecutor);
        httpProxyFactoryBean.afterPropertiesSet();
        target = httpProxyFactoryBean.getObject();
    }

    @Override
    public Class getInterfaceClass() {
        try {
            return Class.forName(serviceName);
        } catch (ClassNotFoundException e) {
            LOGGER.error(e);
        }
        return null;
    }

    @Override
    public Result doInvoker(Invocation invocation) {
        return InvokerUtil.invokeMethod(target, invocation);
    }
}
