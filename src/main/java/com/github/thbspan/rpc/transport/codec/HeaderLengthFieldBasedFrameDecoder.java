package com.github.thbspan.rpc.transport.codec;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class HeaderLengthFieldBasedFrameDecoder extends LengthFieldBasedFrameDecoder {
    public HeaderLengthFieldBasedFrameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
    }

}
