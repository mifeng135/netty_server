package com.game.logic.manager;

import com.game.logic.aoi.AoiSendMsgHelp;
import com.game.logic.aoi.EnterLeftInfo;
import com.game.logic.aoi.Grid;
import com.game.logic.config.MapConfig;
import com.game.logic.model.Player;
import com.game.logic.model.Scene;
import com.game.logic.util.MathUtil;

import java.util.*;


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
     * 获取某个场景下的以gridId为中心的所有player
     *
     * @param sceneId
     * @param gridId
     * @return
     */
    public static List<Player> getPlayerList(int sceneId, int gridId) {
        return sceneMap.get(sceneId).getAoiManager().getPlayerList(gridId);
    }

    public static List<Integer> getPlayerIndexList(int sceneId, int gridId) {
        return sceneMap.get(sceneId).getAoiManager().getPlayerIndexList(gridId);
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

    /***
     *
     * @param sceneId
     * @param oldGridId
     * @param newGridId
     * @return
     */
    public static EnterLeftInfo getEnterLeftList(int sceneId, int oldGridId, int newGridId) {
        Scene scene = sceneMap.get(sceneId);
        List<Grid> leaveGridList = new ArrayList<>(scene.getAoiManager().getGridList(oldGridId));
        List<Grid> enterGridList = new ArrayList<>(scene.getAoiManager().getGridList(newGridId));

        Collection leave = new ArrayList<>(leaveGridList);
        Collection enter = new ArrayList<>(enterGridList);

        leaveGridList.removeAll(enter);
        enterGridList.removeAll(leave);

        EnterLeftInfo enterLeftInfo = new EnterLeftInfo();
        enterLeftInfo.setEnterList(enterGridList);
        enterLeftInfo.setLeaveList(leaveGridList);
        return enterLeftInfo;
    }


    /**
     * 玩家进入某个场景
     *
     * @param sceneId
     * @param player
     */
    public static void playerEnterScene(int sceneId, Player player) {
        Scene scene = sceneMap.get(sceneId);
        int gridId = MathUtil.getGridId(player.getPlayerSceneBean().getPlayerPositionX(), player.getPlayerSceneBean().getPlayerPositionY(),
                scene.getAoiManager().getGridCntX());

        scene.getAoiManager().getGrid(gridId).addPlayer(player);
        List<Player> playerList = scene.getAoiManager().getPlayerList(gridId);
        AoiSendMsgHelp.sendPlayerEnterScene(playerList, player);
    }

    /**
     * 玩家离开场景
     *
     * @param sceneId
     * @param player
     */
    public static void playerLeaveScene(int sceneId, Player player) {
        int gridId = player.getCurrentGridId();
        Scene scene = sceneMap.get(sceneId);
        scene.getAoiManager().getGrid(gridId).removePlayer(player);
        List<Integer> playerList = scene.getAoiManager().getPlayerIndexList(gridId);
        AoiSendMsgHelp.sendPlayerLeaveScene(player, playerList);
    }
}
