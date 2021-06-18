package com.game.logic.model;

import com.game.logic.aoi.AoiManager;
import com.game.logic.config.MapConfig;

import static com.game.logic.Constants.GRID_HEIGHT;
import static com.game.logic.Constants.GRID_WIDTH;


public class Scene {
    private int sceneId;
    private final AoiManager aoiManager;

    public Scene(MapConfig config) {
        sceneId = config.getId();
        aoiManager = new AoiManager(GRID_WIDTH, GRID_HEIGHT, config.getWidth(), config.getHeight());
    }

    public int getSceneId() {
        return sceneId;
    }

    public AoiManager getAoiManager() {
        return aoiManager;
    }
}
