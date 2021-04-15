package com.game.server.controller;


import com.game.server.query.PlayerInfoQuery;
import com.game.server.query.PlayerSceneQuery;
import core.annotation.Ctrl;
import core.annotation.CtrlCmd;
import core.msg.TransferMsg;
import core.util.ProtoUtil;
import io.netty.channel.ChannelHandlerContext;
import protocal.remote.user.CreatePlayerReq;
import protocal.remote.user.CreatePlayerRsp;

import static protocal.MsgConstant.DB_CMD_CREATE_PLAYER_REQ;

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

        int playerResult = PlayerInfoQuery.createPlayer(name, msg.getHeaderProto().getRemoteIp(),openId);
        int secenResult = PlayerSceneQuery.createScene(playerIndex);
    }
}
