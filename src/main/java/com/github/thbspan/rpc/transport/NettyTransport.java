package com.github.thbspan.rpc.transport;

import com.github.thbspan.rpc.common.logger.Logger;
import com.github.thbspan.rpc.common.logger.LoggerFactory;
import com.github.thbspan.rpc.transport.codec.CMessageChannelHandler;
import com.github.thbspan.rpc.transport.codec.HeaderDecoder;
import com.github.thbspan.rpc.transport.codec.HeaderEncoder;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class NettyTransport implements Transport {
    private static final Logger logger = LoggerFactory.getLogger(NettyTransport.class);

    private static final int DEFAULT_HEARTBEAT = 60 * 1000;

    @Override
    public Server bind(String ip, int port) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        int idleTimeout = DEFAULT_HEARTBEAT * 3;
        try {
            //辅助启动类
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workGroup)
                    // 创建的channel为NioServerSocketChannel【nio-ServerSocketChannel】
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_REUSEADDR, Boolean.TRUE)
                    .childOption(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    // 配置accepted的channel
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new LoggingHandler())
                                    .addLast("decode", new HeaderDecoder(1024 * 1024, 37, 4))
                                    .addLast("encode", new HeaderEncoder())
                                    .addLast("server-idle-handler", new IdleStateHandler(0, 0, idleTimeout, MILLISECONDS))
                                    .addLast("cmessage", new CMessageChannelHandler())
                                    //处理IO事件的处理类，处理网络事件
                                    .addLast(new NettyServerHandler());
                        }
                    });
            // 绑定端口后同步等待
            b.bind(ip, port).sync();
            logger.info(String.format("bind to [%s:%d]", ip, port));
        } catch (Exception e) {
            logger.error("init server exception", e);
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
        return new NettyServer(bossGroup, workGroup);
    }

    @Override
    public Client connect(String ip, int port) {
        EventLoopGroup group = new NioEventLoopGroup();
        Channel channel = null;
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("decode", new HeaderDecoder(1024 * 1024, 37, 4));
                            ch.pipeline().addLast("encode", new HeaderEncoder());
                            ch.pipeline().addLast("cmessage", new CMessageChannelHandler());
                            ch.pipeline().addLast(new NettyClientHandler());
                        }

                    });
            logger.info(String.format("connect to [%s:%d]", ip, port));
            ChannelFuture f = b.connect(ip, port).syncUninterruptibly();
            channel = f.channel();
        } catch (Exception e) {
            logger.error("init client exception", e);
            group.shutdownGracefully();
        }
        return new NettyClient(channel, group);
    }
}
