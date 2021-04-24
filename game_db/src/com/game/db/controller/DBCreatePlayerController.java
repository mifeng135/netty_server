package com.game.db.controller;


import bean.player.PlayerBean;
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
import io.netty.channel.ChannelHandlerContext;
import protocal.remote.user.CreatePlayerReq;
import protocal.remote.user.CreatePlayerRsp;

import static com.game.db.constant.GameConstant.MAP_INIT_ID;
import static core.Constants.SQL_MASTER;
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
        boolean playerResult = PlayerInfoQuery.createPlayer(playerBean);

        PlayerSceneBean playerSceneBean = initPlayerScene(playerIndex);
        boolean secenResult = PlayerSceneQuery.createScene(playerSceneBean);

        PlayerRoleBean playerRoleBean = initPlayerRole(playerIndex, job, sex);
        boolean playerRoleResult = PlayerRoleQuery.createPlayerRole(playerRoleBean);

        boolean updateLoginServer = insertLoginServerInfo(playerIndex);

        CreatePlayerRsp createPlayerRsp = new CreatePlayerRsp();
        if (playerResult && secenResult && playerRoleResult && updateLoginServer) {
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

    private boolean insertLoginServerInfo(int playerIndex) {
        LoginPlayerServerInfoBean playerServerInfoBean = new LoginPlayerServerInfoBean();
        playerServerInfoBean.setPlayerIndex(playerIndex);
        playerServerInfoBean.setServerId(PropertiesConfig.serverId);
        playerServerInfoBean = SqlDao.getInstance().getDao().insert(playerServerInfoBean);
        if (playerServerInfoBean != null) {
            return true;
        }
        return false;
    }
}
