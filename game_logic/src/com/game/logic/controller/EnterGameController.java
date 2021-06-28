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

    @CtrlCmd(cmd = MsgConstant.MSG_ENTER_GAME_REQ, beforeCmd = DB_CMD_QUERY_PLAYER_ALL_INFO_REQ)
    public void playerEnterGame(TransferMsg msg) {
        msg.getHeaderProto().setMsgId(MSG_ENTER_GAME_RSP);
        PlayerAllInfoDB playerAllInfoDB = ProtoUtil.deserializer(msg.getDbData(), PlayerAllInfoDB.class);
        if (playerAllInfoDB.getPlayerInfo() == null) {
            MsgUtil.sendErrorMsg(msg.getHeaderProto(), 1);
            return;
        }
        EnterGameRsp enterGameRsp = new EnterGameRsp();
        enterGameRsp.setPlayerAllInfoDB(playerAllInfoDB);
        MsgUtil.sendMsg(msg.getHeaderProto(), enterGameRsp);
    }
}
