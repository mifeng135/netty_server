package com.game.server.controller;

import com.game.server.util.TcpUtil;
import core.annotation.Ctrl;
import core.annotation.CtrlCmd;
import core.msg.TransferMsg;
import core.util.ProtoUtil;
import io.netty.channel.ChannelHandlerContext;
import protocol.remote.bc.SycPositionBC;
import protocol.local.scene.SycPositionReq;

import static protocol.MsgConstant.*;

@Ctrl
public class SycPositioniController {

    @CtrlCmd(cmd = MSG_SYC_POSITION_BC)
    public void syncPositionBc(TransferMsg msg, ChannelHandlerContext context) {
        SycPositionBC sycPositionBC = ProtoUtil.deserializer(msg.getData(), SycPositionBC.class);
        TcpUtil.sendAllClient(MSG_SYC_POSITION_BC, sycPositionBC);
    }

    @CtrlCmd(cmd = MSG_SYC_POSITION_REQ)
    public void syncPosition(TransferMsg msg, ChannelHandlerContext context) {
        SycPositionReq sycPositionReq = ProtoUtil.deserializer(msg.getData(), SycPositionReq.class);
        sycPositionReq.setPlayerIndex(msg.getPlayerIndex());
        TcpUtil.sendToScene(MSG_SYC_POSITION_REQ, sycPositionReq);
    }
}
