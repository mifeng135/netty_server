package com.game.server.controller;


import core.annotation.Ctrl;
import core.annotation.CtrlCmd;
import core.msg.TransferMsg;
import core.util.ProtoUtil;
import io.netty.channel.ChannelHandlerContext;
import protocol.local.PlayerBaseInfoReq;
import static protocol.MsgConstant.*;

@Ctrl
public class PlayerBaseInfoController {

    @CtrlCmd(cmd = MSG_PLAYER_BASE_INFO_REQ)
    public void tcpReq(TransferMsg msg, ChannelHandlerContext context) {
        PlayerBaseInfoReq playerBaseInfoReq = ProtoUtil.deserializer(msg.getData(), PlayerBaseInfoReq.class);
        int playerIndex = playerBaseInfoReq.getPlayerIndex();
    }
}
