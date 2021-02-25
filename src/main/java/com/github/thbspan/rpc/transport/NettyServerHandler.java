package com.github.thbspan.rpc.transport;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.github.thbspan.rpc.invoker.Invocation;
import com.github.thbspan.rpc.invoker.Invoker;
import com.github.thbspan.rpc.provider.Provider;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.DefaultThreadFactory;

public class NettyServerHandler extends SimpleChannelInboundHandler<Request> {
    private final ExecutorService executorService = Executors.newFixedThreadPool(4, new DefaultThreadFactory("worker", true));

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Request request) throws Exception {
        Object data = request.getData();
        if (data instanceof Invocation){
            final Invocation invocation = (Invocation) data;
            executorService.submit(() -> {
                Response response = new Response(request.getId());
                String serviceName = invocation.getInterfaceClass().getName();

                Invoker invoker = Provider.getInvoker(serviceName);

                if (invoker == null) {
                    response.setData(new RuntimeException("can not find invoker of " + serviceName));
                } else {
                    try {
                        response.setData(invoker.doInvoker(invocation));
                    } catch (Throwable e) {
                        response.setData(e);
                    }
                }
                ctx.channel().writeAndFlush(response);
            });
        }
    }
}
