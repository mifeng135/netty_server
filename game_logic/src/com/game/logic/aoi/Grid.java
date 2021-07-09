package com.game.logic.aoi;

import com.game.logic.model.Player;
import lombok.Getter;
import lombok.Setter;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


@Getter
@Setter
public class Grid {

    private int id;
    private float gridPositionX; //横向起始坐标
    private float gridPositionY; //纵向起始坐标
    private Map<Integer, Player> playerMap = new ConcurrentHashMap<>();
    private List<Grid> surroundList = new ArrayList<>();
    private List<Integer> playerIndexList = new ArrayList<>();

    public Grid(int id, float gridPositionX, float gridPositionY) {
        this.id = id;
        this.gridPositionX = gridPositionX;
        this.gridPositionY = gridPositionY;
    }

    /**
     * 在Grid中添加一个player
     * @param player
     */
    public void addPlayer(Player player) {
        playerMap.putIfAbsent(player.getPlayerIndex(), player);
        playerIndexList = playerMap.values().stream().map(Player::getPlayerIndex).collect(Collectors.toList());
    }

    /**
     * 在Grid中移除一个player
     * @param player
     */
    public void removePlayer(Player player) {
        playerMap.remove(player.getPlayerIndex());
        playerIndexList = playerMap.values().stream().map(Player::getPlayerIndex).collect(Collectors.toList());
    }

    /**
     * 获取Grid中所有的player
     * @return
     */
    public List<Player> getPlayerList() {
        return new ArrayList<>(playerMap.values());
    }

    /**
     * 获取Grid中的所有playerIndex
     * @return
     */
    public List<Integer> getPlayerIndexList() {
        return playerIndexList;
    }
}
