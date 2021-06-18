package com.game.logic.model;

import com.game.logic.common.Position;
import com.game.logic.manager.SceneManager;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Player {
    private int playerIndex;
    private String name;
    private String level;
    private int exp;
    private int sceneId = -1;
    private Position position;
    private int currentGridId = -1;

    public Player(int playerIndex, Position position, int sceneId) {
        this.playerIndex = playerIndex;
        this.position = position;
        this.sceneId = sceneId;
        updatePlayerPosition(position, true);
    }

    /**
     * 玩家移动时候更新位置 或者刚刚进入游戏的时候
     *
     * @param position   位置信息
     * @param firstEnter 是否刚刚进入游戏
     */
    public void updatePlayerPosition(Position position, boolean firstEnter) {
        this.position = position;
        Scene scene = SceneManager.getScene(sceneId);
        int newGridId = position.getGridId(scene.getAoiManager().getGridCntX());
        if (currentGridId != newGridId) {
            SceneManager.changeGrid(this, currentGridId, newGridId);
            currentGridId = newGridId;
        }
        sendSyncMsg(false, firstEnter);
    }

    /**
     * 切换场景的时候调用
     *
     * @param newSceneId 新的场景id
     * @param position   新的位置
     */
    public void updatePlayerPosition(int newSceneId, Position position) {
        this.position = position;
        Scene scene = SceneManager.getScene(newSceneId);
        int newGridId = position.getGridId(scene.getAoiManager().getGridCntX());
        boolean changeScene = false;
        if (sceneId != newSceneId) {
            SceneManager.changeScene(this, sceneId, newSceneId, currentGridId, newGridId);
            this.sceneId = newSceneId;
            changeScene = true;
        }
        sendSyncMsg(changeScene, false);
    }

    /**
     * 发送 通过消息
     *
     * @param changeScene 是否是切换场景
     * @param firstEnter  是否是切换场景
     */
    private void sendSyncMsg(boolean changeScene, boolean firstEnter) {
        List<Player> playerList = SceneManager.getPlayerList(sceneId, currentGridId);
        // TODO: 2021/6/18  send  player change scene
    }
}
