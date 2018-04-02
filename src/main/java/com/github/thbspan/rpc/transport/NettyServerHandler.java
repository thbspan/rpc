package com.github.thbspan.rpc.transport;

import com.github.thbspan.rpc.invoker.Invocation;
import com.github.thbspan.rpc.invoker.Invoker;
import com.github.thbspan.rpc.provider.Provider;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.nio.NioEventLoopGroup;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    private NioEventLoopGroup loop = new NioEventLoopGroup();
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        final Request request = (Request) msg;
        Object data = request.getData();
        if (data instanceof Invocation){
            final Invocation invocation = (Invocation) data;
            loop.submit(() -> {
                Response reponse = new Response(request.getId());
                String serviceName = invocation.getInterfaceClass().getName();

                Invoker invoker = Provider.invokers.get(serviceName);

                if (invoker == null) {
                    reponse.setData(new RuntimeException("can not find invoker of " + serviceName));
                } else {
                    try {
                        reponse.setData(invoker.doInvoker(invocation));
                    } catch (Throwable e) {
                        reponse.setData(e);
                    }
                }
                ctx.writeAndFlush(reponse);
            });
        }
    }
}
