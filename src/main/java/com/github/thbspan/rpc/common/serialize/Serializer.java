package com.github.thbspan.rpc.common.serialize;

import com.github.thbspan.rpc.transport.Request;
import com.github.thbspan.rpc.transport.Response;

public interface Serializer {
    Request unSerializeRequest(byte[] bytes);
    Response unSerializeResponse(byte[] bytes);

    byte[] serialize(Request request);
    byte[] serialize(Response response);
}
