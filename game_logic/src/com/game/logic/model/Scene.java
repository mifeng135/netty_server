package com.game.logic.model;

import com.game.logic.common.Position;
import com.game.logic.common.Rect;
import com.game.logic.common.WatchTowerIndex;
import com.game.logic.config.MapConfig;
import com.game.logic.manager.WatchTower;


import java.util.ArrayList;
import java.util.List;

import static com.game.logic.Constants.WATCH_TOWER_HEIGHT;
import static com.game.logic.Constants.WATCH_TOWER_WIDTH;

public class Scene {

    private int sceneId;
    private int width;
    private int height;
    private int watchTowerWidthCount;
    private int watchTowerHeightCount;


    private WatchTower[][] watchTowerList;

    public Scene(MapConfig config) {
        sceneId = config.getId();
        width = config.getWidth();
        height = config.getHeight();
        watchTowerWidthCount = (int) Math.ceil(width / WATCH_TOWER_WIDTH);
        watchTowerHeightCount = (int) Math.ceil(height / WATCH_TOWER_HEIGHT);
        createWatchTowerList();
    }

    private void createWatchTowerList() {
        watchTowerList = new WatchTower[watchTowerWidthCount][watchTowerHeightCount];
        for (int i = 0; i < watchTowerWidthCount; i++) {
            for (int j = 0; j < watchTowerHeightCount; j++) {
                int positionX = i * WATCH_TOWER_WIDTH;
                int positionY = j * WATCH_TOWER_HEIGHT;
                WatchTower watchTower = new WatchTower(i, j, new Position(positionX, positionY));
                watchTowerList[i][j] = watchTower;
            }
        }
    }

    public int getSceneId() {
        return sceneId;
    }

    public WatchTower[][] getWatchTowerList() {
        return watchTowerList;
    }
}
