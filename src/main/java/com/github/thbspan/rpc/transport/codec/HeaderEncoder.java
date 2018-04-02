package com.github.thbspan.rpc.transport.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class HeaderEncoder extends MessageToByteEncoder<CMessage> {
    @Override
    protected void encode(ChannelHandlerContext ctx, CMessage msg, ByteBuf out) throws Exception {
        CHeader header=msg.getHeader();

        out.writeByte(HeaderDecoder.PACKAGE_TAG);
        out.writeByte(header.getEncode());
        out.writeByte(header.getEncrypt());
        out.writeByte(header.getExtend1());
        out.writeByte(header.getExtend2());
        out.writeBytes(header.getSessionId().getBytes());
        out.writeInt(header.getLength());
        out.writeInt(header.getCommandId());
        out.writeBytes(msg.getData());
    }
}
