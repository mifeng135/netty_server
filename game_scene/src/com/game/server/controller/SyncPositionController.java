package com.game.server.controller;

import com.game.server.util.TcpUtil;
import core.annotation.Ctrl;
import core.annotation.CtrlCmd;
import core.msg.TransferMsg;
import core.util.ProtoUtil;
import io.netty.channel.ChannelHandlerContext;
import protocol.local.scene.SycPositionReq;
import protocol.local.scene.SycPositionRsp;

import static protocol.MsgConstant.*;

@Ctrl
public class SyncPositionController {

    @CtrlCmd(cmd = MSG_SYC_POSITION_REQ)
    public void openSession(TransferMsg msg, ChannelHandlerContext context) {
        SycPositionReq sycPositionReq = ProtoUtil.deserializer(msg.getData(), SycPositionReq.class);
        SycPositionRsp sycPositionRsp = new SycPositionRsp();
        sycPositionRsp.setPosition(sycPositionReq.getPosition());
        sycPositionRsp.setMove(true);
        TcpUtil.sendToCenter(MSG_SYC_POSITION_RSP, sycPositionRsp);
    }
}
