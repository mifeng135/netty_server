package com.game.logic.controller;

import com.game.logic.util.MsgUtil;
import core.annotation.Ctrl;
import core.annotation.CtrlCmd;
import core.msg.TransferMsg;
import core.util.ProtoUtil;
import protocal.local.scene.SyncPositionReq;
import protocal.local.scene.SyncPositionRsp;

import static constants.MsgConstant.*;


@Ctrl
public class SyncPositionController {

    @CtrlCmd(cmd = MSG_SYNC_POSITION_REQ)
    public void openSession(TransferMsg msg) {
        SyncPositionReq sycPositionReq = ProtoUtil.deserializer(msg.getData(), SyncPositionReq.class);
        SyncPositionRsp syncPositionRsp = new SyncPositionRsp();
        syncPositionRsp.setPosition(sycPositionReq.getPosition());
        msg.getHeaderProto().setMsgId(MSG_SYNC_POSITION_RSP);
        syncPositionRsp.setMove(true);
        MsgUtil.sendMsg(msg.getHeaderProto(), syncPositionRsp);
    }
}
