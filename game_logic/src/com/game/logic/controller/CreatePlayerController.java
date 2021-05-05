package com.game.logic.controller;

import com.game.logic.util.MsgUtil;
import core.annotation.Ctrl;
import core.annotation.CtrlCmd;
import core.msg.TransferMsg;
import core.util.ProtoUtil;
import org.asynchttpclient.Response;
import protocal.MsgConstant;
import protocal.local.db.player.PlayerAllInfoDB;
import protocal.remote.user.CreatePlayerRsp;

import static protocal.MsgConstant.*;

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
