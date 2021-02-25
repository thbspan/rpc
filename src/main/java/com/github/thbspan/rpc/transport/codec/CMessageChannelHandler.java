package com.github.thbspan.rpc.transport.codec;

import java.util.UUID;

import com.github.thbspan.rpc.common.serialize.Serializer;
import com.github.thbspan.rpc.common.serialize.navtivejava.JdkSerializer;
import com.github.thbspan.rpc.common.utils.StringUtils;
import com.github.thbspan.rpc.transport.Request;
import com.github.thbspan.rpc.transport.Response;
import com.github.thbspan.rpc.transport.heartbeat.HeartbeatMessageHandler;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

public class CMessageChannelHandler extends ChannelDuplexHandler {
    private final Serializer serializer = new JdkSerializer();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof CMessage) {
            CMessage message = (CMessage) msg;
            CHeader header = message.getHeader();
            byte type = header.getExtend1();
            if (type == 0) {
                // Request
                super.channelRead(ctx, serializer.unSerializeRequest(message.getData()));
            } else if (type == 1) {
                // Response
                super.channelRead(ctx, serializer.unSerializeResponse(message.getData()));
            } else if (type == 9) {
                // heart beat
                ctx.channel().write(HeartbeatMessageHandler.createHeartbeatMessage());
            }
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof Request) {
            Request request = (Request) msg;
            CMessage message = getCMessage(request);
            ctx.writeAndFlush(message);
        } else if (msg instanceof Response) {
            Response response = (Response) msg;
            CMessage message = getCMessage(response);
            ctx.writeAndFlush(message);
        }

    }

    private CMessage getCMessage(Request request) {
        byte[] data = serializer.serialize(request);
        //session 32bit
        CHeader header = new CHeader(StringUtils.remove(UUID.randomUUID().toString(), '-'));
        header.setLength(data.length);
        header.setExtend1((byte) 0);
        return new CMessage(header, data);
    }

    private CMessage getCMessage(Response response) {
        byte[] data = serializer.serialize(response);
        //session 32bit
        CHeader header = new CHeader(StringUtils.remove(UUID.randomUUID().toString(), '-'));
        header.setExtend1((byte) 1);
        header.setLength(data.length);
        return new CMessage(header, data);
    }
}
