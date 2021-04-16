package com.game.logic.manager;

import com.game.logic.model.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerManager {

    private static Map<Integer, Player> playerMap = new ConcurrentHashMap<>();

    public Player getPlayer(int playerIndex) {
        return playerMap.get(playerIndex);
    }

    public void removePlayer(int playerIndex) {
        playerMap.remove(playerIndex);
    }
}
