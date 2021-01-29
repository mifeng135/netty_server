package com.game.server.controller;


import com.game.server.Config;
import core.annotation.Ctrl;
import core.annotation.CtrlCmd;
import core.manager.SocketManager;
import core.msg.TransferMsg;
import core.util.ProtoUtil;
import core.util.SocketUtil;
import io.netty.channel.ChannelHandlerContext;
import protocol.scene.SycPositionBC;
import protocol.scene.SycPositionReq;

import static protocol.MsgConstant.*;

@Ctrl
public class SycPositioniController {

    @CtrlCmd(cmd = MSG_SYC_POSITION_BC)
    public void syncPositionBc(TransferMsg msg, ChannelHandlerContext context) {
        SycPositionBC sycPositionBC = ProtoUtil.deserializer(msg.getData(),SycPositionBC.class);
        SocketUtil.sendRemoteAllTcpMsg(MSG_SYC_POSITION_BC, sycPositionBC);
    }

    @CtrlCmd(cmd = MSG_SYC_POSITION_REQ)
    public void syncPosition(TransferMsg msg, ChannelHandlerContext context) {
        SycPositionReq sycPositionReq = ProtoUtil.deserializer(msg.getData(), SycPositionReq.class);
        sycPositionReq.setPlayerIndex(msg.getSocketIndex());
        SocketUtil.sendLoaclTcpMsgToServer(Config.CONNECT_GATE_CENTER_SOCKET_INDEX, MSG_SYC_POSITION_REQ, sycPositionReq);
    }
}
