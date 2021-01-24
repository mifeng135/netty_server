package com.game.server.controller;


import core.annotation.Ctrl;
import core.annotation.CtrlCmd;
import core.msg.TransferMsg;
import core.util.ProtoUtil;
import core.util.SocketUtil;
import io.netty.channel.ChannelHandlerContext;
import protocol.scene.SycPositionBC;
import protocol.scene.SycPositionReq;
import protocol.scene.SycPositionRsp;

import static com.game.server.Config.CONNECT_GATE_CENTER_SERVER_KEY;
import static com.game.server.Config.CONNECT_SCENE_CENTER_SERVER_KEY;
import static protocol.MsgConstant.*;

@Ctrl
public class SycPositioniController {

    @CtrlCmd(cmd = MSG_SYC_POSITION_RSP)
    public void openSession(TransferMsg msg, ChannelHandlerContext context) {
        SycPositionRsp sycPositionRsp = ProtoUtil.deserializer(msg.getData(), SycPositionRsp.class);
        SycPositionBC sycPositionBC = new SycPositionBC();
        sycPositionBC.setPosition(sycPositionRsp.getPosition());
        sycPositionBC.setPlayerIndex(sycPositionRsp.getPlayerIndex());
        SocketUtil.sendLocalTcpMsgToConnection(CONNECT_GATE_CENTER_SERVER_KEY, MSG_SYC_POSITION_BC, 0, sycPositionBC);
    }


    @CtrlCmd(cmd = MSG_SYC_POSITION_REQ)
    public void syncPosition(TransferMsg msg, ChannelHandlerContext context) {
        SycPositionReq sycPositionReq = ProtoUtil.deserializer(msg.getData(), SycPositionReq.class);
        SocketUtil.sendLocalTcpMsgToConnection(CONNECT_SCENE_CENTER_SERVER_KEY, MSG_SYC_POSITION_REQ, 0, sycPositionReq);
    }
}
