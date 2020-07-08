package com.game.server.room;

import com.game.server.constant.Constant;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Administrator on 2020/7/3.
 */
public class Room {

    private int gameState = Constant.GAME_STATE_WAIT;
    private List<Player> roomPlayer = new CopyOnWriteArrayList<>();
    private int roomId;

    private int maxPlayer = 2;

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

    public void removePlyaer(int playerId) {
        for (int i = 0; i < roomPlayer.size(); i++) {
            if (roomPlayer.get(i).getId() == playerId) {
                roomPlayer.remove(i);
                break;
            }
        }
    }
    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public boolean getFull() {
        return roomPlayer.size() >= maxPlayer;
    }

    public void updatePlayerReadyState(int id,int state) {
        for (int i = 0; i < roomPlayer.size(); i++) {
            if (roomPlayer.get(i).getId() == id) {
                roomPlayer.get(i).setReady(state);
                break;
            }
        }
    }

    public boolean getPlayerAllReady() {
        for (int i = 0; i < roomPlayer.size(); i++) {
            if (roomPlayer.get(i).getReady() != 1) {
                return false;
            }
        }
        return true;
    }

}
