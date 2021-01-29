package com.game.server.controller;

import com.game.server.Config;
import core.annotation.Ctrl;
import core.annotation.CtrlCmd;
import core.msg.TransferMsg;
import core.util.ProtoUtil;
import core.util.SocketUtil;
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
        sycPositionRsp.setPlayerIndex(sycPositionReq.getPlayerIndex());
        sycPositionRsp.setMove(true);
        SocketUtil.sendLoaclTcpMsgToServer(Config.CONNECT_SCENE_CENTER_SOCKET_INDEX, MSG_SYC_POSITION_RSP, sycPositionRsp);
    }
}
