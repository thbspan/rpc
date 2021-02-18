package com.github.thbspan.rpc.transport;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.DefaultPromise;

public class NettyClientHandler extends SimpleChannelInboundHandler<Response> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Response response) throws Exception {
        String id = response.getId();

        DefaultPromise<Response> promise = NettyClient.FUTURES.remove(id);
        // 收到请求
        promise.setSuccess(response);
    }
}
