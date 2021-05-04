package com.game.logic.manager;


import com.game.logic.common.Position;
import com.game.logic.common.Rect;
import com.game.logic.common.WatchTowerIndex;
import com.game.logic.model.Player;


import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.game.logic.Constants.WATCH_TOWER_HEIGHT;
import static com.game.logic.Constants.WATCH_TOWER_WIDTH;

/**
 * 灯塔 横向是1000 纵向是500
 */
public class WatchTower {

    private WatchTowerIndex watchTowerIndex; //数组索引 x
    private Rect rect;
    private Map<Integer, Player> playerMap = new ConcurrentHashMap<>();

    public WatchTower(int xIndex, int yIndex, Position position) {
        watchTowerIndex = new WatchTowerIndex(xIndex,yIndex);
        rect = new Rect(position.getX(), position.getY(), WATCH_TOWER_WIDTH, WATCH_TOWER_HEIGHT);
    }

    public Rect getRect() {
        return rect;
    }

    public WatchTowerIndex getWatchTowerIndex() {
        return watchTowerIndex;
    }

    public void addPlayer(Player player) {
        playerMap.put(player.getPlayerIndex(), player);
    }

    public void removePlayer(int playerIndex) {
        playerMap.remove(playerIndex);
    }

    public Set<Integer> getPlayerIndexSet() {
        return playerMap.keySet();
    }
}
