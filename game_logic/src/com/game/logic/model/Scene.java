package com.game.logic.model;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Scene {

    public static Map<Integer, Player> playerMap = new ConcurrentHashMap<>();

    public static void addPlayer(Player player) {
        playerMap.put(player.getPlayerIndex(), player);
    }

    public static void removePlayer(int playerIndex) {
        playerMap.remove(playerIndex);
    }
}
