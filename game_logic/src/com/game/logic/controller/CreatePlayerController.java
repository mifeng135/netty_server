package com.game.logic.controller;

import com.game.logic.manager.PlayerManager;
import com.game.logic.model.Player;
import com.game.logic.util.MsgUtil;
import constants.MsgConstant;
import core.annotation.ctrl.Ctrl;
import core.annotation.ctrl.CtrlCmd;
import core.msg.TransferMsg;
import core.util.ProtoUtil;
import protocal.local.db.player.PlayerAllInfoDB;
import protocal.remote.user.CreatePlayerRsp;

import static constants.MsgConstant.DB_CMD_CREATE_PLAYER_REQ;
import static constants.MsgConstant.MSG_CREATE_PLAYER_RSP;


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
        msg.getHeaderProto().setMsgId(MSG_CREATE_PLAYER_RSP);
        MsgUtil.sendMsg(msg.getHeaderProto(), createPlayerRsp);

        Player player = new Player(playerAllInfoDB.getPlayerScene(), playerAllInfoDB.getPlayerRole(), playerAllInfoDB.getPlayerInfo());
        PlayerManager.addPlayer(player);
    }
}
