package com.github.thbspan.rpc.common.serialize.navtivejava;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.github.thbspan.rpc.common.logger.Logger;
import com.github.thbspan.rpc.common.logger.LoggerFactory;
import com.github.thbspan.rpc.common.serialize.Serializer;
import com.github.thbspan.rpc.transport.Request;
import com.github.thbspan.rpc.transport.Response;

public class JdkSerializer implements Serializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(JdkSerializer.class);

    @Override
    public Request unSerializeRequest(byte[] bytes) {
        try (ByteArrayInputStream is = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(is)) {
            return (Request) ois.readObject();
        } catch (Exception e) {
            LOGGER.error(e);
            return null;
        }
    }

    @Override
    public Response unSerializeResponse(byte[] bytes) {
        try (ByteArrayInputStream is = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(is)) {
            return (Response) ois.readObject();
        } catch (Exception e) {
            LOGGER.error(e);
            return null;
        }
    }

    @Override
    public byte[] serialize(Request request) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(out)) {
            oos.writeObject(request);
            return out.toByteArray();
        } catch (IOException e) {
            LOGGER.error(e);
            return new byte[0];
        }
    }

    @Override
    public byte[] serialize(Response response) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(out)) {
            oos.writeObject(response);
            return out.toByteArray();
        } catch (IOException e) {
            LOGGER.error(e);
            return new byte[0];
        }
    }
}
