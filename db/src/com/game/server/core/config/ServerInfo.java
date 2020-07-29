package com.game.server.core.config;

import com.game.server.core.zero.ReceiveAdapter;

/**
 * Created by Administrator on 2020/7/28.
 */
public class ServerInfo {
    private String ip;
    private int port;
    private byte serverKey;
    private String name;
    private ReceiveAdapter adapter;

    public ServerInfo(String ip, int port, byte serverKey, String name) {
        this.ip = ip;
        this.port = port;
        this.serverKey = serverKey;
        this.name = name;
    }

    public ServerInfo(String ip, int port, byte serverKey, String name,ReceiveAdapter adapter) {
        this.ip = ip;
        this.port = port;
        this.serverKey = serverKey;
        this.name = name;
        this.adapter = adapter;
    }

    public byte getServerKey() {
        return serverKey;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public String getName() {
        return name;
    }

    public ReceiveAdapter getAdapter() {
        return adapter;
    }
}
