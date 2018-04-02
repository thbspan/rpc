package com.github.thbspan.rpc.transport.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.CorruptedFrameException;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class HeaderDecoder extends LengthFieldBasedFrameDecoder {
    public static final int HEAD_LENGTH = 45;
    public static final byte PACKAGE_TAG = 0X01;

    public HeaderDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        in.markReaderIndex();//保存readerIndex，调用resetReaderIndex()可以将marked的index回复为readerIndex
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
        int commandId = in.readInt();//命令

        //创建header
        CHeader header = new CHeader(encode, encrypt, extend1, extend2, sessionId, length, commandId);
        //创建message
        ByteBuf buf=in.readBytes(length);

        CMessage message = new CMessage(header, buf.array());
        //添加到输出
        return message;
    }
}
