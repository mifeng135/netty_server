package com.game.db.controller;


import bean.player.PlayerBean;
import bean.player.PlayerRoleBean;
import bean.player.PlayerSceneBean;
import bean.player.PlayerServerInfoBean;
import com.game.db.PropertiesConfig;
import com.game.db.query.PlayerInfoQuery;
import com.game.db.query.PlayerRoleQuery;
import com.game.db.query.PlayerSceneQuery;
import com.game.db.util.MsgUtil;
import core.annotation.Ctrl;
import core.annotation.CtrlCmd;
import core.annotation.SqlAnnotation;
import core.msg.TransferMsg;
import core.util.ProtoUtil;
import io.netty.channel.ChannelHandlerContext;
import protocal.remote.user.CreatePlayerReq;
import protocal.remote.user.CreatePlayerRsp;

import static com.game.db.constant.GameConstant.MAP_INIT_ID;
import static com.game.db.constant.SqlCmdConstant.PLAYER_SERVER_INFO_INSERT;
import static protocal.MsgConstant.*;

@Ctrl
public class DBCreatePlayerController {

    @CtrlCmd(cmd = DB_CMD_CREATE_PLAYER_REQ)
    public void dbCreatePlayer(TransferMsg msg, ChannelHandlerContext context) {
        CreatePlayerReq createPlayerReq = ProtoUtil.deserializer(msg.getData(), CreatePlayerReq.class);
        int playerIndex = msg.getHeaderProto().getPlayerIndex();
        int job = createPlayerReq.getJob();
        int sex = createPlayerReq.getSex();
        String name = createPlayerReq.getName();
        String openId = createPlayerReq.getOpenId();

        PlayerBean playerBean = initPlayer(playerIndex, name, msg.getHeaderProto().getRemoteIp(), openId);
        int playerResult = PlayerInfoQuery.createPlayer(playerBean);

        PlayerSceneBean playerSceneBean = initPlayerScene(playerIndex);
        int secenResult = PlayerSceneQuery.createScene(playerSceneBean);

        PlayerRoleBean playerRoleBean = initPlayerRole(playerIndex, job, sex);
        int playerRoleResult = PlayerRoleQuery.createPlayerRole(playerRoleBean);


        int updateLoginServer = updateLoginServerInfo(playerIndex);

        CreatePlayerRsp createPlayerRsp = new CreatePlayerRsp();
        if (playerResult == 1 && secenResult == 1 && playerRoleResult == 1 && updateLoginServer == 1) {
            createPlayerRsp.setHasRole(true);
            createPlayerRsp.setPlayerRole(playerRoleBean);
            createPlayerRsp.setPlayerScene(playerSceneBean);
            MsgUtil.sendMsg(context, msg.getHeaderProto(), createPlayerRsp);
            return;
        }
        createPlayerRsp.setHasRole(true);
        MsgUtil.sendMsg(context, msg.getHeaderProto(), new Object());
    }

    private PlayerBean initPlayer(int playerIndex, String name, String remoteIp, String openId) {
        PlayerBean playerBean = new PlayerBean();
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

    private int updateLoginServerInfo(int playerIndex) {
        PlayerServerInfoBean playerServerInfoBean = new PlayerServerInfoBean();
        playerServerInfoBean.setPlayerIndex(playerIndex);
        playerServerInfoBean.setServerId(PropertiesConfig.serverId);
        return SqlAnnotation.getInstance().executeCommitSqlWithServerId(PropertiesConfig.loginServerId, PLAYER_SERVER_INFO_INSERT, playerServerInfoBean);
    }
}