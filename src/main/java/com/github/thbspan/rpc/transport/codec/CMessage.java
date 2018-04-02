package com.github.thbspan.rpc.transport.codec;

public class CMessage {
    /** 头消息 **/
    private CHeader header;
    /** 数据 **/
    private byte[] data;

    public  CMessage(){

    }

    public  CMessage(CHeader header){
        this.header=header;
    }

    public CMessage(CHeader header, byte[] data) {
        this.header = header;
        this.data = data;
    }

    public CHeader getHeader() {
        return header;
    }

    public void setHeader(CHeader header) {
        this.header = header;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
