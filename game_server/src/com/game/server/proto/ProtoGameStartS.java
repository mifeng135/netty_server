package com.game.server.proto;

import java.util.List;

/**
 * Created by Administrator on 2020/7/8.
 */
public class ProtoGameStartS {

    private List<ProtoPlayer> playerList;

    public List<ProtoPlayer> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<ProtoPlayer> playerList) {
        this.playerList = playerList;
    }
}
