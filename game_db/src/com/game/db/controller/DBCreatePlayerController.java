package com.game.db.controller;


import bean.player.PlayerInfoBean;
import bean.player.PlayerRoleBean;
import bean.player.PlayerSceneBean;
import bean.login.LoginPlayerServerInfoBean;
import com.game.db.PropertiesConfig;
import com.game.db.query.PlayerInfoQuery;
import com.game.db.query.PlayerRoleQuery;
import com.game.db.query.PlayerSceneQuery;
import com.game.db.util.MsgUtil;
import core.annotation.Ctrl;
import core.annotation.CtrlCmd;
import core.msg.TransferMsg;
import core.sql.SqlDao;
import core.util.ProtoUtil;
import protocal.local.db.player.PlayerAllInfoDB;
import protocal.remote.user.CreatePlayerReq;

import static com.game.db.constant.GameConstant.MAP_INIT_ID;
import static com.game.db.constant.GameConstant.SQL_KEY_LOGIN;
import static protocal.MsgConstant.*;

@Ctrl
public class DBCreatePlayerController {

    @CtrlCmd(cmd = DB_CMD_CREATE_PLAYER_REQ)
    public void dbCreatePlayer(TransferMsg msg) {
        CreatePlayerReq createPlayerReq = ProtoUtil.deserializer(msg.getData(), CreatePlayerReq.class);
        int playerIndex = msg.getHeaderProto().getPlayerIndex();
        int job = createPlayerReq.getJob();
        int sex = createPlayerReq.getSex();
        String name = createPlayerReq.getName();
        String openId = createPlayerReq.getOpenId();

        PlayerInfoBean playerInfoBean = initPlayer(playerIndex, name, msg.getHeaderProto().getRemoteIp(), openId);
        boolean playerResult = PlayerInfoQuery.createPlayer(playerInfoBean);

        PlayerSceneBean playerSceneBean = initPlayerScene(playerIndex);
        boolean sceneResult = PlayerSceneQuery.createScene(playerSceneBean);

        PlayerRoleBean playerRoleBean = initPlayerRole(playerIndex, job, sex);
        boolean playerRoleResult = PlayerRoleQuery.createPlayerRole(playerRoleBean);

        boolean updateLoginServer = insertLoginServerInfo(playerIndex);

        PlayerAllInfoDB playerAllInfoDB = new PlayerAllInfoDB();
        if (playerResult && sceneResult && playerRoleResult && updateLoginServer) {
            playerAllInfoDB.setPlayerRole(playerRoleBean);
            playerAllInfoDB.setPlayerInfo(playerInfoBean);
            playerAllInfoDB.setPlayerScene(playerSceneBean);
            MsgUtil.sendToLogic(msg, playerAllInfoDB);
            return;
        }
        MsgUtil.sendToLogic(msg, playerAllInfoDB);
    }

    private PlayerInfoBean initPlayer(int playerIndex, String name, String remoteIp, String openId) {
        PlayerInfoBean playerBean = new PlayerInfoBean();
        playerBean.setServerId(PropertiesConfig.serverId);
        playerBean.setPlayerIndex(playerIndex);
        playerBean.setName(name);
        playerBean.setOpenId(openId);
        playerBean.setLoginIp(remoteIp);
        return playerBean;
    }

    private PlayerSceneBean initPlayerScene(int playerIndex) {
        PlayerSceneBean playerScene = new PlayerSceneBean();
        playerScene.setSceneId(MAP_INIT_ID);
        playerScene.setPlayerPositionX(400);
        playerScene.setPlayerPositionY(400);
        playerScene.setPlayerIndex(playerIndex);
        return playerScene;
    }

    private PlayerRoleBean initPlayerRole(int playerIndex, int job, int sex) {
        PlayerRoleBean playerRole = new PlayerRoleBean();
        playerRole.setPlayerIndex(playerIndex);
        playerRole.setJob(job);
        playerRole.setSex(sex);
        return playerRole;
    }

    private boolean insertLoginServerInfo(int playerIndex) {
        LoginPlayerServerInfoBean playerServerInfoBean = new LoginPlayerServerInfoBean();
        playerServerInfoBean.setPlayerIndex(playerIndex);
        playerServerInfoBean.setServerId(PropertiesConfig.serverId);
        playerServerInfoBean = SqlDao.getInstance().getDao(SQL_KEY_LOGIN).insert(playerServerInfoBean);
        if (playerServerInfoBean != null) {
            return true;
        }
        return false;
    }
}
