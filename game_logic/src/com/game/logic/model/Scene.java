package com.game.logic.model;


import com.game.logic.manager.WatchTower;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Scene {

    private int sceneId;
    private Map<Integer, Player> playerMap = new ConcurrentHashMap<>();
    private List<WatchTower> watchTowerList = new ArrayList<>();

    public Scene(int id) {
        sceneId = id;
    }

    public void createWatchTower() {

    }
}
