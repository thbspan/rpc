package com.github.thbspan.rpc.transport;

import java.io.Serializable;

public class Response implements Serializable{
    private static final long serialVersionUID = -7245057246218165279L;
    private String id;
    private Object data;

    public Response(String id) {
        this.id = id;
    }

    public Response(String id,Object data) {
        this.data = data;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
