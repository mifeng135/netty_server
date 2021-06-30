package com.game.logic.model;

import bean.db.player.PlayerInfoBean;
import bean.db.player.PlayerRoleBean;
import bean.db.player.PlayerSceneBean;
import com.game.logic.aoi.AoiSendMsgHelp;
import com.game.logic.aoi.EnterLeftInfo;
import com.game.logic.manager.SceneManager;
import com.game.logic.util.MathUtil;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


@Setter
@Getter
public class Player {

    private static final Logger logger = LoggerFactory.getLogger(Player.class);

    private int playerIndex;
    private int sceneId;
    private int currentGridId;
    private PlayerSceneBean playerSceneBean;
    private PlayerRoleBean playerRoleBean;
    private PlayerInfoBean playerInfoBean;

    public Player(PlayerSceneBean playerSceneBean, PlayerRoleBean playerRoleBean, PlayerInfoBean playerInfoBean) {
        playerIndex = playerInfoBean.getId();
        sceneId = playerSceneBean.getSceneId();
        Scene scene = SceneManager.getScene(sceneId);
        currentGridId = MathUtil.getGridId(playerSceneBean.getPlayerPositionX(), playerSceneBean.getPlayerPositionX(), scene.getAoiManager().getGridCntX());
        scene.getAoiManager().getGrid(currentGridId).addPlayer(this);
        this.playerSceneBean = playerSceneBean;
        this.playerRoleBean = playerRoleBean;
        this.playerInfoBean = playerInfoBean;
    }

    /**
     * 玩家移动时候更新位置 或者刚刚进入游戏的时候
     */
    public void updatePlayerPosition(int x, int y, boolean firstMove) {
        playerSceneBean.setPlayerPositionX(x);
        playerSceneBean.setPlayerPositionY(y);
        Scene scene = SceneManager.getScene(sceneId);
        int newGridId = MathUtil.getGridId(x, y, scene.getAoiManager().getGridCntX());
        if (currentGridId != newGridId) {
            SceneManager.changeGrid(this, currentGridId, newGridId);
            EnterLeftInfo enterLeftInfo = SceneManager.getEnterLeftList(sceneId, currentGridId, newGridId);
            currentGridId = newGridId;
            AoiSendMsgHelp.sendPlayerChangeGrid(enterLeftInfo, this);
        }
        List<Integer> playerList = SceneManager.getPlayerIndexList(sceneId, currentGridId);
        AoiSendMsgHelp.sendSyncPosition(x, y, firstMove, playerList, playerIndex);
    }

    /**
     * 切换场景的时候调用
     *
     * @param newSceneId 新的场景id
     */
    public void updatePlayerPosition(int newSceneId, int x, int y) {
        playerSceneBean.setPlayerPositionX(x);
        playerSceneBean.setPlayerPositionY(y);
        Scene scene = SceneManager.getScene(newSceneId);
        int newGridId = MathUtil.getGridId(x, y, scene.getAoiManager().getGridCntX());
        if (sceneId != newSceneId) {
            SceneManager.changeScene(this, sceneId, newSceneId, currentGridId, newGridId);
            sceneId = newSceneId;
            currentGridId = newGridId;
        }
    }
}
