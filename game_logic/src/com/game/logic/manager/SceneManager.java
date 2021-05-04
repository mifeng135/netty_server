package com.game.logic.manager;

import com.game.logic.common.Position;
import com.game.logic.common.Rect;
import com.game.logic.common.WatchTowerIndex;
import com.game.logic.config.MapConfig;
import com.game.logic.model.Player;
import com.game.logic.model.Scene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SceneManager {

    private static Map<Integer, Scene> sceneMap = new HashMap<>();

    public static void initSceneMap() {
        List<MapConfig> mapConfigList = MapConfig.mapConfigList;
        for (int i = 0; i < mapConfigList.size(); i++) {
            Scene scene = new Scene(mapConfigList.get(i));
            sceneMap.put(scene.getSceneId(), scene);
        }
    }

    public static Scene getScene(int sceneId) {
        return sceneMap.get(sceneId);
    }

    public static WatchTower getWatchTower(Player player) {
        Scene scene = getScene(player.getSceneId());
        Position position = player.getPosition();
        for (int i = 0; i < scene.getWatchTowerList().length; i++) {
            for (int j = 0; i < scene.getWatchTowerList()[i].length; j++) {
                WatchTower watchTower = scene.getWatchTowerList()[i][j];
                Rect rect = watchTower.getRect();
                if (rect.containsPoint(position)) {
                    return watchTower;
                }
            }
        }
        return null;
    }

    public static List<Integer> getPlayerNearbyList(Player player) {
        Scene scene = getScene(player.getSceneId());
        List<Integer> playerList = new ArrayList<>();

        WatchTower[][] watchTowers = scene.getWatchTowerList();
        WatchTower watchTower = getWatchTower(player);
        if (watchTower != null) {
            WatchTowerIndex watchTowerIndex = watchTower.getWatchTowerIndex();
            int xIndex = watchTowerIndex.getX();
            int yIndex = watchTowerIndex.getY();
            for (int i = -1; i < 2; i++) {
                int x = xIndex + i;
                if (x >= 0) {
                    for (int j = -1; j < 2; j++) {
                        int y = yIndex + j;
                        if (y >= 0) {
                            playerList.addAll(watchTowers[x][y].getPlayerIndexSet());
                        }
                    }
                }
            }
        }
        return playerList;
    }
}
