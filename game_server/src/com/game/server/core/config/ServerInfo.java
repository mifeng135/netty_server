package com.game.server.core.config;

/**
 * Created by Administrator on 2020/7/28.
 */
public class ServerInfo {
    private String ip;
    private int port;
    private int serverKey;

    public ServerInfo(String ip, int port, int serverKey) {
        this.ip = ip;
        this.port = port;
        this.serverKey = serverKey;
    }

    public int getServerKey() {
        return serverKey;
    }

    public void setServerKey(int serverKey) {
        this.serverKey = serverKey;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

}
