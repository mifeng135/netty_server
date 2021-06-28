package com.game.logic.controller;

import com.game.logic.util.MsgUtil;
import constants.MsgConstant;
import core.annotation.ctrl.Ctrl;
import core.annotation.ctrl.CtrlCmd;
import core.msg.TransferMsg;
import core.util.ProtoUtil;
import protocal.local.db.player.PlayerAllInfoDB;
import protocal.remote.user.CreatePlayerRsp;

import static constants.MsgConstant.DB_CMD_CREATE_PLAYER_REQ;


@Ctrl
public class CreatePlayerController {

    @CtrlCmd(cmd = MsgConstant.MSG_CREATE_PLAYER_REQ, beforeCmd = DB_CMD_CREATE_PLAYER_REQ)
    public void createPlayer(TransferMsg msg) {
        PlayerAllInfoDB playerAllInfoDB = ProtoUtil.deserializer(msg.getDbData(), PlayerAllInfoDB.class);
        CreatePlayerRsp createPlayerRsp = new CreatePlayerRsp();
        if (playerAllInfoDB == null) {
            MsgUtil.sendErrorMsg(msg.getHeaderProto(), 1);
            return;
        }
        createPlayerRsp.setSuccess(true);
        createPlayerRsp.setPlayerAllInfoDB(playerAllInfoDB);
        MsgUtil.sendMsg(msg.getHeaderProto(), createPlayerRsp);
    }
}
