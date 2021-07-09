package com.game.db.controller;


import bean.db.player.PlayerInfoBean;
import bean.db.player.PlayerRoleBean;
import bean.db.player.PlayerSceneBean;
import com.game.db.PropertiesConfig;
import com.game.db.util.MsgUtil;
import constants.TableKey;
import core.annotation.ctrl.Ctrl;
import core.annotation.ctrl.CtrlCmd;
import core.msg.TransferMsg;
import core.util.Ins;
import core.util.ProtoUtil;
import protocal.local.db.player.PlayerAllInfoDB;
import protocal.remote.user.CreatePlayerReq;

import static com.game.db.constant.GameConstant.MAP_INIT_ID;
import static constants.MsgConstant.*;

@Ctrl
public class CreatePlayerController {

    @CtrlCmd(cmd = DB_CMD_CREATE_PLAYER_REQ)
    public void dbCreatePlayer(TransferMsg msg) {
        CreatePlayerReq createPlayerReq = ProtoUtil.deserializer(msg.getData(), CreatePlayerReq.class);
        int playerIndex = msg.getHeaderProto().getPlayerIndex();
        int job = createPlayerReq.getJob();
        int sex = createPlayerReq.getSex();
        String name = createPlayerReq.getName();
        String openId = createPlayerReq.getOpenId();

        PlayerInfoBean playerInfoBean = initPlayer(playerIndex, name, msg.getHeaderProto().getRemoteIp(), openId);
        Ins.redis().put(TableKey.GAME_PLAYER, playerInfoBean);

        PlayerSceneBean playerSceneBean = initPlayerScene(playerIndex);
        Ins.redis().put(TableKey.GAME_PLAYER_SCENE, playerSceneBean);

        PlayerRoleBean playerRoleBean = initPlayerRole(playerIndex, job, sex);
        Ins.redis().put(TableKey.GAME_PLAYER_ROLE, playerRoleBean);

        insertLoginServerInfo(playerIndex);

        PlayerAllInfoDB playerAllInfoDB = new PlayerAllInfoDB();
        playerAllInfoDB.setPlayerRole(playerRoleBean);
        playerAllInfoDB.setPlayerInfo(playerInfoBean);
        playerAllInfoDB.setPlayerScene(playerSceneBean);
        MsgUtil.sendMsg(msg, playerAllInfoDB);
    }

    private PlayerInfoBean initPlayer(int playerIndex, String name, String remoteIp, String openId) {
        PlayerInfoBean playerBean = new PlayerInfoBean();
        playerBean.setServerId(PropertiesConfig.serverId);
        playerBean.setId(playerIndex);
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
        playerScene.setId(playerIndex);
        return playerScene;
    }

    private PlayerRoleBean initPlayerRole(int playerIndex, int job, int sex) {
        PlayerRoleBean playerRole = new PlayerRoleBean();
        playerRole.setId(playerIndex);
        playerRole.setJob(job);
        playerRole.setSex(sex);
        return playerRole;
    }

    private boolean insertLoginServerInfo(int playerIndex) {
        return false;
    }
}
