package com.game.server.model;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Scene {
    public Map<Integer, Player> playerMap = new ConcurrentHashMap<>();

    public void addPlayer(Player player) {
        playerMap.put(player.getPlayerIndex(), player);
    }

    public void removePlayer(int playerIndex) {
        playerMap.remove(playerIndex);
    }
}
