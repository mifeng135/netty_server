package com.game.server.room;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2020/7/3.
 */
public class Room {

    private int gameState = -1;
    private List<Player> roomPlayer = new ArrayList<>();



    public List<Player> getRoomPlayer() {
        return roomPlayer;
    }

    public void setRoomPlayer(List<Player> roomPlayer) {
        this.roomPlayer = roomPlayer;
    }

    public int getGameState() {
        return gameState;
    }

    public void setGameState(int gameState) {
        this.gameState = gameState;
    }

    public void addPlayer(Player player) {
        roomPlayer.add(player);
    }
}
