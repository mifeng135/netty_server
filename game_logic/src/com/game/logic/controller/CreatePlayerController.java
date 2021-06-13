package com.game.logic.controller;

import com.game.logic.util.MsgUtil;
import constants.MsgConstant;
import core.annotation.ctrl.Ctrl;
import core.annotation.ctrl.CtrlCmd;
import core.msg.TransferMsg;
import core.util.ProtoUtil;
import protocal.local.db.player.PlayerAllInfoDB;
import protocal.remote.user.CreatePlayerRsp;

import static constants.MsgConstant.*;


@Ctrl
public class CreatePlayerController {

    @CtrlCmd(cmd = MsgConstant.MSG_CREATE_PLAYER_REQ)
    public void createPlayer(TransferMsg msg) {
        msg.getHeaderProto().setMsgId(DB_CMD_CREATE_PLAYER_REQ);
        MsgUtil.sendToDB(msg);
    }

    @CtrlCmd(cmd = MsgConstant.DB_CMD_CREATE_PLAYER_RSP)
    public void dbCreatePlayerRsp(TransferMsg msg) {
        msg.getHeaderProto().setMsgId(MSG_CREATE_PLAYER_RSP);
        PlayerAllInfoDB playerAllInfoDB = ProtoUtil.deserializer(msg.getData(), PlayerAllInfoDB.class);
        CreatePlayerRsp createPlayerRsp = new CreatePlayerRsp();
        if (playerAllInfoDB != null) {
            createPlayerRsp.setSuccess(true);
            createPlayerRsp.setPlayerAllInfoDB(playerAllInfoDB);
            MsgUtil.sendMsg(msg.getHeaderProto(), createPlayerRsp);
        } else {
            MsgUtil.sendErrorMsg(msg.getHeaderProto(), 1);
        }
    }
}
