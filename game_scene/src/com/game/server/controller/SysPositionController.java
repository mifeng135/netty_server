package com.game.server.controller;

import com.game.server.util.TcpUtil;
import core.annotation.Ctrl;
import core.annotation.CtrlCmd;
import core.msg.TransferMsg;
import core.util.ProtoUtil;
import io.netty.channel.ChannelHandlerContext;
import protocol.scene.SycPositionReq;
import protocol.scene.SycPositionRsp;

import static protocol.MsgConstant.*;

@Ctrl
public class SysPositionController {

    @CtrlCmd(cmd = MSG_SYC_POSITION_REQ)
    public void openSession(TransferMsg msg, ChannelHandlerContext context) {
        SycPositionReq sycPositionReq = ProtoUtil.deserializer(msg.getData(), SycPositionReq.class);
        SycPositionRsp sycPositionRsp = new SycPositionRsp();
        sycPositionRsp.setPosition(sycPositionReq.getPosition());
        sycPositionRsp.setMove(true);
        TcpUtil.sendToCenter(msg.getPlayerIndex(), MSG_SYC_POSITION_RSP, sycPositionRsp);
    }
}
