package com.github.thbspan.rpc.transport;

import com.github.thbspan.rpc.invoker.Invocation;
import com.github.thbspan.rpc.invoker.Result;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class NettyClient implements Client{
    static final Map<String, DefaultPromise<Response>> FUTURES = new ConcurrentHashMap<>();
    private final NioEventLoopGroup loop = new NioEventLoopGroup();
    private final Channel channel;
    private final EventLoopGroup group;
    public NettyClient(Channel channel, EventLoopGroup group){
        this.channel = channel;
        this.group = group;
    }
    @Override
    public Result send(Invocation invocation) {
        Request request = new Request(invocation);

        channel.writeAndFlush(request);
        final DefaultPromise<Response> promise = new DefaultPromise<>(loop.next());

        FUTURES.put(request.getId(), promise);

        Result result = new Result();
        try {
            Response response = promise.get(60, TimeUnit.SECONDS);//默认1秒超时，response中的data是result
            return (Result) response.getData();
        } catch (Exception e) {
            result.setException(e);
        }
        return result;
    }

    @Override
    public void close() throws Exception {
        channel.close();
        group.shutdownGracefully();
    }
}
