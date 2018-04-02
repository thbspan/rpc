package com.github.thbspan.rpc.transport;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.DefaultPromise;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Response response = (Response) msg;
        String id = response.getId();

        DefaultPromise<Response> promise = NettyClient.FUTURES.remove(id);
        promise.setSuccess(response); // 收到请求
    }
}
