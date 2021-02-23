package com.github.thbspan.rpc.transport;

import io.netty.channel.EventLoopGroup;

public class NettyServer implements Server {
    private final EventLoopGroup bossGroup;
    private final EventLoopGroup workGroup;

    public NettyServer(EventLoopGroup bossGroup, EventLoopGroup workGroup) {
        this.bossGroup = bossGroup;
        this.workGroup = workGroup;
    }

    @Override
    public void close() throws Exception {
        this.bossGroup.shutdownGracefully();
        this.workGroup.shutdownGracefully();
    }
}
