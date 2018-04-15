package com.github.thbspan.rpc.transport.codec;

import com.github.thbspan.rpc.common.logger.Logger;
import com.github.thbspan.rpc.common.logger.LoggerFactory;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.CorruptedFrameException;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class HeaderDecoder extends LengthFieldBasedFrameDecoder {
    Logger logger = LoggerFactory.getLogger(HeaderDecoder.class);
    public static final int HEAD_LENGTH = 45;// 5 + 32 + 4 + 4
    public static final byte PACKAGE_TAG = 0X01;

    public HeaderDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        logger.info("ByteBuf in={}"+in);
        if (in == null){
            return null;
        }
        if (in.readableBytes() < HEAD_LENGTH){
            throw new RuntimeException("msg size was too short");
        }
        byte tag = in.readByte();// 读一个字节
        if (tag != PACKAGE_TAG) {//若第一个自己不是0X01开头，抛出异常。
            throw new CorruptedFrameException("非法协议包");
        }
        byte encode = in.readByte();//第一个字节
        byte encrypt = in.readByte();//第一个字节
        byte extend1 = in.readByte();//第一个字节
        byte extend2 = in.readByte();//第一个字节
        byte sessionByte[] = new byte[32];//session

        in.readBytes(sessionByte);//读取到sessionByte

        String sessionId = new String(sessionByte);//session

        int length = in.readInt();//header的leangth，指定body的长度
        logger.info("length={}"+length);
        int commandId = in.readInt();//命令

        //创建header
        CHeader header = new CHeader(encode, encrypt, extend1, extend2, sessionId, length, commandId);
        logger.info("header={}"+header);
        //创建message
        ByteBuf buf=in.readBytes(length);
//        ByteBuf buf = Unpooled.buffer(in.readableBytes());
        if (buf.hasArray()){
            logger.error("buf={}"+buf);
            //添加到输出
            return new CMessage(header, buf.array());
        } else {
            byte[] data = new byte[length];
            buf.readBytes(data);
            return new CMessage(header, data);
        }

    }
}
