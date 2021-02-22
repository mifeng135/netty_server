package com.game.server.controller;

import com.game.server.util.TcpUtil;
import core.annotation.Ctrl;
import core.annotation.CtrlCmd;
import core.msg.TransferMsg;
import core.util.ProtoUtil;
import io.netty.channel.ChannelHandlerContext;
import protocol.local.scene.SyncPositionRsp;
import protocol.remote.bc.SyncPositionBC;
import protocol.local.scene.SyncPositionReq;

import static protocol.MsgConstant.*;

@Ctrl
public class SycPositioniController {

    @CtrlCmd(cmd = MSG_SYC_POSITION_RSP)
    public void syncPositionBc(TransferMsg msg, ChannelHandlerContext context) {
        SyncPositionRsp syncPositionRsp = ProtoUtil.deserializer(msg.getData(), SyncPositionRsp.class);
        SyncPositionBC sycPositionBC = new SyncPositionBC();
        sycPositionBC.setPlayerIndex(syncPositionRsp.getPlayerIndex());
        sycPositionBC.setPosition(syncPositionRsp.getPosition());
        TcpUtil.sendAllClient(MSG_SYC_POSITION_BC, sycPositionBC);
    }

    @CtrlCmd(cmd = MSG_SYC_POSITION_REQ)
    public void syncPosition(TransferMsg msg, ChannelHandlerContext context) {
        SyncPositionReq sycPositionReq = ProtoUtil.deserializer(msg.getData(), SyncPositionReq.class);
        sycPositionReq.setPlayerIndex(msg.getPlayerIndex());
        TcpUtil.sendToScene(MSG_SYC_POSITION_REQ, sycPositionReq);
    }
}
