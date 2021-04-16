package com.game.db.controller;


import bean.player.PlayerServerInfoBean;
import com.game.db.query.PlayerInfoQuery;
import com.game.db.query.PlayerRoleQuery;
import com.game.db.query.PlayerSceneQuery;
import com.game.db.util.HttpUtil;
import core.annotation.Ctrl;
import core.annotation.CtrlCmd;
import core.annotation.SqlAnnotation;
import core.msg.TransferMsg;
import core.util.ConfigUtil;
import core.util.ProtoUtil;
import io.netty.channel.ChannelHandlerContext;
import protocal.remote.user.CreatePlayerReq;
import protocal.remote.user.CreatePlayerRsp;

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
        int playerResult = PlayerInfoQuery.createPlayer(playerIndex, name, msg.getHeaderProto().getRemoteIp(), openId);
        int secenResult = PlayerSceneQuery.createScene(playerIndex);
        int playerRoleResult = PlayerRoleQuery.createPlayerRole(playerIndex, job, sex);
        int updateLoginServer = updateLoginServerInfo(playerIndex);
        if (playerResult == 1 && secenResult == 1 && playerRoleResult == 1 && updateLoginServer == 1) {
            CreatePlayerRsp createPlayerRsp = new CreatePlayerRsp();
            createPlayerRsp.setName(name);
            createPlayerRsp.setPlayerPositionX(100);
            createPlayerRsp.setPlayerPositionY(100);
            createPlayerRsp.setSceneId(1);
            createPlayerRsp.setJob(job);
            createPlayerRsp.setSex(1);
            msg.getHeaderProto().setSuccess(true);
            HttpUtil.sendMsg(context, msg.getHeaderProto(), createPlayerRsp);
            return;
        }
        msg.getHeaderProto().setSuccess(false);
        HttpUtil.sendMsg(context, msg.getHeaderProto(), new Object());
    }

    private int updateLoginServerInfo(int playerIndex) {
        int serverId = ConfigUtil.getInt("server_id");
        int loginServerId = ConfigUtil.getInt("login_server_id");
        PlayerServerInfoBean playerServerInfoBean = new PlayerServerInfoBean();
        playerServerInfoBean.setPlayerIndex(playerIndex);
        playerServerInfoBean.setServerId(serverId);
        return SqlAnnotation.getInstance().executeCommitSqlWithServerId(loginServerId, PLAYER_SERVER_INFO_INSERT, playerServerInfoBean);
    }
}
