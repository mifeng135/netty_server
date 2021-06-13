package com.game.logic.controller;

import com.game.logic.util.MsgUtil;
import core.annotation.ctrl.Ctrl;
import core.annotation.ctrl.CtrlCmd;
import core.msg.TransferMsg;
import core.util.ProtoUtil;
import constants.MsgConstant;
import protocal.local.db.player.PlayerAllInfoDB;
import protocal.remote.user.EnterGameRsp;

import static constants.MsgConstant.*;

@Ctrl
public class EnterGameController {

    @CtrlCmd(cmd = MsgConstant.MSG_ENTER_GAME_REQ)
    public void playerEnterGame(TransferMsg msg) {
        msg.getHeaderProto().setMsgId(DB_CMD_QUERY_ALL_PLAYER_INFO_REQ);
        MsgUtil.sendToDB(msg);
    }

    @CtrlCmd(cmd = MsgConstant.DB_CMD_QUERY_ALL_PLAYER_INFO_RSP)
    public void playerAllInfo(TransferMsg msg) {
        msg.getHeaderProto().setMsgId(MSG_ENTER_GAME_RSP);
        PlayerAllInfoDB playerAllInfoDB = ProtoUtil.deserializer(msg.getData(), PlayerAllInfoDB.class);
        EnterGameRsp enterGameRsp = new EnterGameRsp();
        if (playerAllInfoDB.getPlayerInfo() != null) {
            enterGameRsp.setPlayerAllInfoDB(playerAllInfoDB);
            MsgUtil.sendMsg(msg.getHeaderProto(), enterGameRsp);
        } else {
            MsgUtil.sendErrorMsg(msg.getHeaderProto(), 1);
        }
    }
}
