package com.github.thbspan.rpc.transport.codec;

import com.github.thbspan.rpc.common.logger.Logger;
import com.github.thbspan.rpc.common.logger.LoggerFactory;
import com.github.thbspan.rpc.common.serialize.Serializer;
import com.github.thbspan.rpc.common.serialize.navtivejava.JdkSerializer;
import com.github.thbspan.rpc.transport.Request;
import com.github.thbspan.rpc.transport.Response;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

public class CMessageChannelHandler extends ChannelDuplexHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(CMessageChannelHandler.class);
    private Serializer serializer = new JdkSerializer();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        CMessage  message = (CMessage) msg;
        CHeader header = message.getHeader();
        byte type = header.getExtend1();
        if(type == 0) { // Request
            super.channelRead(ctx, serializer.unSerializeRequest(message.getData()));
        }else {// Response
            super.channelRead(ctx, serializer.unSerializeResponse(message.getData()));
        }
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {

        if (msg instanceof Request) {
            Request request = (Request) msg;
            CMessage message = getCMessage(request);
            ctx.writeAndFlush(message);
        }

        if (msg instanceof Response) {
            Response response = (Response) msg;
            CMessage message = getCMessage(response);
            ctx.writeAndFlush(message);
        }

    }

    private CMessage getCMessage(Request request) {
        byte data [] = serializer.serialize(request);
        CHeader header=new CHeader("12345678901234567890123456789012");//session 32bit
        header.setLength(data.length);
        header.setExtend1((byte)0);
        return new CMessage(header, data);
    }

    private CMessage getCMessage(Response response) {
        byte  data[] = serializer.serialize(response);
        CHeader header=new CHeader("12345678901234567890123456789012");//session 32bit
        header.setExtend1((byte)1);
        header.setLength(data.length);
        return new CMessage(header,data);
    }
}
