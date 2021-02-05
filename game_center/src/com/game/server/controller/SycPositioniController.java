package com.game.server.controller;


import com.game.server.util.TcpUtil;
import core.annotation.Ctrl;
import core.annotation.CtrlCmd;
import core.msg.TransferMsg;
import core.util.ProtoUtil;
import io.netty.channel.ChannelHandlerContext;
import protocol.scene.SycPositionBC;
import protocol.scene.SycPositionReq;
import protocol.scene.SycPositionRsp;

import static protocol.MsgConstant.*;

@Ctrl
public class SycPositioniController {

    @CtrlCmd(cmd = MSG_SYC_POSITION_RSP)
    public void openSession(TransferMsg msg, ChannelHandlerContext context) {
        SycPositionRsp sycPositionRsp = ProtoUtil.deserializer(msg.getData(), SycPositionRsp.class);
        SycPositionBC sycPositionBC = new SycPositionBC();
        sycPositionBC.setPosition(sycPositionRsp.getPosition());
        sycPositionBC.setPlayerIndex(msg.getPlayerIndex());
        TcpUtil.sendToGate(msg.getPlayerIndex(), MSG_SYC_POSITION_BC, sycPositionBC);
    }
}