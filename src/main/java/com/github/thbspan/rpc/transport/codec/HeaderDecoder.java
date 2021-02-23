package com.github.thbspan.rpc.transport.codec;

import java.nio.charset.StandardCharsets;

import com.github.thbspan.rpc.common.logger.Logger;
import com.github.thbspan.rpc.common.logger.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.CorruptedFrameException;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class HeaderDecoder extends LengthFieldBasedFrameDecoder {
    private final Logger logger = LoggerFactory.getLogger(HeaderDecoder.class);
    /**
     * 5 + 32 + 4 + 4
     * 1个字节头部 + 4个字节其他信息
     * 32个字节sessionId
     * 4个字节长度字段
     * 4个字节命令字段
     */
    public static final int HEAD_LENGTH = 45;
    public static final byte PACKAGE_TAG = 0X01;

    public HeaderDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        logger.info("ByteBuf in={}" + in);
        if (in == null) {
            return null;
        }
        if (in.readableBytes() < HEAD_LENGTH) {
            throw new RuntimeException("msg size was too short");
        }
        // 读一个字节
        byte tag = in.readByte();
        if (tag != PACKAGE_TAG) {
            //若第一个自己不是0X01开头，抛出异常
            throw new CorruptedFrameException("非法协议包");
        }
        // 下一个字节
        byte encode = in.readByte();
        // 下一个字节
        byte encrypt = in.readByte();
        // 下一个字节
        byte extend1 = in.readByte();
        // 下一个字节
        byte extend2 = in.readByte();
        // session信息
        byte[] sessionByte = new byte[32];
        // 读取到sessionByte
        in.readBytes(sessionByte);
        //session
        String sessionId = new String(sessionByte, StandardCharsets.UTF_8);
        //header中的length字段，指定body的长度
        int length = in.readInt();
        logger.info("length=" + length);
        // 命令
        int commandId = in.readInt();

        // 创建header
        CHeader header = new CHeader(encode, encrypt, extend1, extend2, sessionId, length, commandId);
        logger.info("header={}" + header);
        // 创建message
        ByteBuf buf = in.readSlice(length);
        // ByteBuf buf = Unpooled.buffer(in.readableBytes());
        if (buf.hasArray()) {
            logger.error("buf={}" + buf);
            // 添加到输出
            return new CMessage(header, buf.array());
        } else {
            byte[] data = new byte[length];
            buf.readBytes(data);
            return new CMessage(header, data);
        }
    }
}
