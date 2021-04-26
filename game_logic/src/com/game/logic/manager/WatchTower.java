package com.game.logic.manager;


import com.game.logic.common.Rect;
import com.game.logic.model.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 灯塔
 */
public class WatchTower {

    private Rect rect;
    private Map<Integer, Player> playerMap = new ConcurrentHashMap<>();

    public WatchTower() {

    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }

    public void addPlayer(Player player) {
        playerMap.put(player.getPlayerIndex(), player);
    }

    public void removePlayer(int playerIndex) {
        playerMap.remove(playerIndex);
    }

}
