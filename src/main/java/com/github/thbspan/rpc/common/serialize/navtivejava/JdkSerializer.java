package com.github.thbspan.rpc.common.serialize.navtivejava;

import com.github.thbspan.rpc.common.logger.Logger;
import com.github.thbspan.rpc.common.logger.LoggerFactory;
import com.github.thbspan.rpc.common.serialize.Serializer;
import com.github.thbspan.rpc.transport.Request;
import com.github.thbspan.rpc.transport.Response;

import java.io.*;

public class JdkSerializer implements Serializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(JdkSerializer.class);

    @Override
    public Request unSerializeRequest(byte[] bytes) {
        ByteArrayInputStream is = new ByteArrayInputStream(bytes);
        ObjectInputStream ois;
        try {
            ois = new ObjectInputStream(is);
            return (Request) ois.readObject();
        } catch (Exception e) {
            LOGGER.error(e);
            return null;
        }
    }

    @Override
    public Response unSerializeResponse(byte[] bytes) {
        ByteArrayInputStream is = new ByteArrayInputStream(bytes);
        ObjectInputStream ois;
        try {
            ois = new ObjectInputStream(is);
            return (Response) ois.readObject();
        } catch (Exception e) {
            LOGGER.error(e);
            return null;
        }
    }

    @Override
    public byte[] serialize(Request request) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {

            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(request);
        } catch (IOException e) {
            LOGGER.error(e);
        }
        return out.toByteArray();
    }

    @Override
    public byte[] serialize(Response response) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {

            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }
}
