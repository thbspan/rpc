package com.github.thbspan.rpc.transport.heartbeat;

import java.util.UUID;

import com.github.thbspan.rpc.common.utils.StringUtils;
import com.github.thbspan.rpc.transport.codec.CHeader;
import com.github.thbspan.rpc.transport.codec.CMessage;

public class HeartbeatMessageHandler {

    public static CMessage createHeartbeatMessage() {
        CHeader header = new CHeader(StringUtils.remove(UUID.randomUUID().toString(), '-'));
        header.setLength(0);
        header.setExtend1((byte) 9);
        return new CMessage(header, null);
    }
}
