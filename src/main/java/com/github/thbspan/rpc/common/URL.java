package com.github.thbspan.rpc.common;

import java.io.Serializable;
import java.util.Objects;

public class URL implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String protocol;

    private final String host;

    private final int port;

    private final String path;

    public URL(String protocol, String host, int port, String path) {
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        this.path = path;
    }

    public static URL valueOf(String url){
        // TODO test
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        URL url = (URL) o;
        return port == url.port &&
                Objects.equals(protocol, url.protocol) &&
                Objects.equals(host, url.host) &&
                Objects.equals(path, url.path);
    }

    @Override
    public int hashCode() {

        return Objects.hash(protocol, host, port, path);
    }

    @Override
    public String toString() {
        return "URL{" +
                "protocol='" + protocol + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", path='" + path + '\'' +
                '}';
    }

    public String getProtocol() {
        return protocol;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getPath() {
        return path;
    }
}
