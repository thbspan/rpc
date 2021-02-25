package com.github.thbspan.rpc.transport.codec;

import java.io.Serializable;

public class CHeader implements Serializable {
    private static final long serialVersionUID = -1733092414601148393L;
    /** 数据编码格式。已定义：0：UTF-8，1：GBK，2：GB2312，3：ISO8859-1 **/
    private byte encode;
    /** 加密类型。0表示不加密 **/
    private byte encrypt;
    /**
     * 消息类型：
     * 0 Request 消息
     * 1 Response 消息
     * 9 HeartBeat 消息
     */
    private byte extend1;
    /** 用于扩展协议。暂未定义任何值 **/
    private byte extend2;
    /** 会话ID **/
    private String sessionId;//32位
    /** 数据包长 **/
    private int length;
    /** 命令 **/
    private int commandId;

    public CHeader clone(){
        try {
            return (CHeader) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public CHeader(){

    }

    public CHeader(String sessionId){
        this.encode=0;
        this.encrypt=0;
        this.sessionId=sessionId;
    }

    public CHeader(byte encode,byte encrypt,
                   byte extend1,byte extend2,
                   String sessionId,int length,int commandId ){
        this.encode = encode;
        this.encrypt = encrypt;
        this.extend1 = extend1;
        this.extend2 = extend2;
        this.sessionId = sessionId;
        this.length = length;
        this.commandId = commandId;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CHeader [encode=").append(encode).append(", encrypt=").append(encrypt).append(", extend1=")
                .append(extend1).append(", extend2=").append(extend2).append(", sessionId=").append(sessionId)
                .append(", length=").append(length).append(", commandId=").append(commandId).append("]");
        return builder.toString();
    }

    public byte getEncode() {
        return encode;
    }

    public void setEncode(byte encode) {
        this.encode = encode;
    }

    public byte getEncrypt() {
        return encrypt;
    }

    public void setEncrypt(byte encrypt) {
        this.encrypt = encrypt;
    }

    public byte getExtend1() {
        return extend1;
    }

    public void setExtend1(byte extend1) {
        this.extend1 = extend1;
    }

    public byte getExtend2() {
        return extend2;
    }

    public void setExtend2(byte extend2) {
        this.extend2 = extend2;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getCommandId() {
        return commandId;
    }

    public void setCommandId(int commandId) {
        this.commandId = commandId;
    }
}
