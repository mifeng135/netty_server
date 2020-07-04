package com.game.server.proto;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2020/7/4.
 */


public class ProtoLoginS {
    private int ret;
    private int playerIndex;
    private String ip;

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}

