package com.github.thbspan.rpc.transport;

import io.netty.channel.EventLoopGroup;

public class NettyServer implements Server {
    private EventLoopGroup bossGroup;
    private EventLoopGroup workGroup;

    public NettyServer(EventLoopGroup bossGroup, EventLoopGroup workGroup) {
        this.bossGroup = bossGroup;
        this.workGroup = workGroup;
    }

    public void close(){
        this.bossGroup.shutdownGracefully();
        this.workGroup.shutdownGracefully();
    }
}
