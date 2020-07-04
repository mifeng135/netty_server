package com.game.server.proto;

/**
 * Created by Administrator on 2020/7/4.
 */
public class ProtoLinkSynS {

    private String ip;
    private int connectCount;

    public int getConnectCount() {
        return connectCount;
    }

    public void setConnectCount(int connectCount) {
        this.connectCount = connectCount;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }


}
