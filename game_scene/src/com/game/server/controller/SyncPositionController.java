package com.game.server.controller;

import com.game.server.util.TcpUtil;
import core.annotation.Ctrl;
import core.annotation.CtrlCmd;
import core.msg.TransferMsg;
import core.util.ProtoUtil;
import io.netty.channel.ChannelHandlerContext;
import protocol.local.scene.SyncPositionReq;
import protocol.local.scene.SyncPositionRsp;

import static protocol.MsgConstant.*;

@Ctrl
public class SyncPositionController {

    @CtrlCmd(cmd = MSG_SYC_POSITION_REQ)
    public void openSession(TransferMsg msg, ChannelHandlerContext context) {
        SyncPositionReq sycPositionReq = ProtoUtil.deserializer(msg.getData(), SyncPositionReq.class);
        SyncPositionRsp syncPositionRsp = new SyncPositionRsp();
        syncPositionRsp.setPosition(sycPositionReq.getPosition());
        syncPositionRsp.setPlayerIndex(sycPositionReq.getPlayerIndex());
        syncPositionRsp.setMove(true);
        TcpUtil.sendToGate(MSG_SYC_POSITION_RSP, syncPositionRsp);
    }
}
