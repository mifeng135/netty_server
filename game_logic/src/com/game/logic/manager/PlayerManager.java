package com.game.logic.manager;

import com.game.logic.model.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerManager {

    private static Map<Integer, Player> playerMap = new ConcurrentHashMap<>();

    public static Player getPlayer(int playerIndex) {
        return playerMap.get(playerIndex);
    }

    public static void removePlayer(int playerIndex) {
        playerMap.remove(playerIndex);
    }

    public static void addPlayer(Player player) {
        playerMap.putIfAbsent(player.getPlayerIndex(), player);
    }
}
