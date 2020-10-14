package com.github.thbspan.rpc.transport;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class Request implements Serializable{
    private static final long serialVersionUID = -8516812983027091043L;
    private static final AtomicInteger count = new AtomicInteger(0);
    private String id;
    private Object data;

    public Request(Object data) {
        this.data = data;
        this.id = genrateId();
    }
    private String genrateId() {
        return "req$" + count.incrementAndGet();
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
