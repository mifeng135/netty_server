package com.game.server.controller;

import com.game.server.util.TcpUtil;
import core.annotation.Ctrl;
import core.annotation.CtrlCmd;
import core.msg.TransferMsg;
import core.util.ProtoUtil;
import io.netty.channel.ChannelHandlerContext;
import protocal.local.scene.SyncPositionReq;
import protocal.local.scene.SyncPositionRsp;

import static protocal.MsgConstant.*;

@Ctrl
public class SyncPositionController {

    @CtrlCmd(cmd = MSG_SYNC_POSITION_REQ)
    public void openSession(TransferMsg msg, ChannelHandlerContext context) {
        SyncPositionReq sycPositionReq = ProtoUtil.deserializer(msg.getData(), SyncPositionReq.class);
        SyncPositionRsp syncPositionRsp = new SyncPositionRsp();
        syncPositionRsp.setPosition(sycPositionReq.getPosition());
        msg.getHeaderProto().setMsgId(MSG_SYNC_POSITION_RSP);
        syncPositionRsp.setMove(true);
        TcpUtil.sendMsg(msg.getHeaderProto(), syncPositionRsp);
    }
}
