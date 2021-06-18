package com.game.logic.manager;

import com.game.logic.aoi.Grid;
import com.game.logic.config.MapConfig;
import com.game.logic.model.Player;
import com.game.logic.model.Scene;

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

    /**
     * 通过sceneId gridId 获取 Grid
     *
     * @param sceneId
     * @param gridId
     * @return
     */
    public static Grid getGrid(int sceneId, int gridId) {
        return sceneMap.get(sceneId).getAoiManager().getGrid(gridId);
    }

    /**
     * 获取某个场景下的
     *
     * @param sceneId
     * @param gridId
     * @return
     */
    public static List<Player> getPlayerList(int sceneId, int gridId) {
        return sceneMap.get(sceneId).getAoiManager().playerList(gridId);
    }


    public static void changeGrid(Player player, int oldGridId, int newGridId) {
        int sceneId = player.getSceneId();
        Scene scene = sceneMap.get(sceneId);
        scene.getAoiManager().getGrid(oldGridId).removePlayer(player);
        scene.getAoiManager().getGrid(newGridId).addPlayer(player);
    }

    public static void changeScene(Player player, int oldSceneId, int newSceneId, int oldGridId, int newGridId) {
        Scene oldScene = sceneMap.get(oldSceneId);
        oldScene.getAoiManager().getGrid(oldGridId).removePlayer(player);
        Scene newScene = sceneMap.get(newSceneId);
        newScene.getAoiManager().getGrid(newGridId).addPlayer(player);
    }
}
