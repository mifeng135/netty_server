package com.game.logic.aoi;

import com.game.logic.model.Player;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Getter
@Setter
public class Grid {

    private int id;
    private float gridPositionX; //横向起始坐标
    private float gridPositionY; //纵向起始坐标
    private Map<Integer, Player> playerMap = new ConcurrentHashMap<>();
    private List<Grid> surroundList = new ArrayList<>();

    public Grid(int id, float gridPositionX, float gridPositionY) {
        this.id = id;
        this.gridPositionX = gridPositionX;
        this.gridPositionY = gridPositionY;
    }

    public void addPlayer(Player player) {
        playerMap.put(player.getPlayerIndex(), player);
    }

    public void removePlayer(Player player) {
        playerMap.remove(player.getPlayerIndex());
    }

    public List<Player> getPlayerList() {
        return new ArrayList<>(playerMap.values());
    }
}