package com.game.logic.controller;

import com.game.logic.util.MsgUtil;
import core.annotation.ctrl.Ctrl;
import core.annotation.ctrl.CtrlCmd;
import core.msg.TransferMsg;
import core.netty.asyncHttp.AsyncHttp;
import core.util.ProtoUtil;
import constants.MsgConstant;
import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.Response;
import protocal.local.db.player.PlayerAllInfoDB;
import protocal.remote.user.EnterGameRsp;

import static constants.MsgConstant.*;

@Ctrl
public class EnterGameController extends AsyncCompletionHandler<Integer> {

    @CtrlCmd(cmd = MsgConstant.MSG_ENTER_GAME_REQ)
    public void playerEnterGame(TransferMsg msg) {
        msg.getHeaderProto().setMsgId(DB_CMD_QUERY_PLAYER_ALL_INFO_REQ);
        AsyncHttp.getInstance().postAsync(msg.getHeaderProto(), msg.getData(), this);
    }

    @Override
    public Integer onCompleted(Response response) throws Exception {
        TransferMsg msg = ProtoUtil.decodeDBHttpMsg(response.getResponseBodyAsBytes());
        msg.getHeaderProto().setMsgId(MSG_ENTER_GAME_RSP);
        PlayerAllInfoDB playerAllInfoDB = ProtoUtil.deserializer(msg.getData(), PlayerAllInfoDB.class);
        EnterGameRsp enterGameRsp = new EnterGameRsp();
        if (playerAllInfoDB.getPlayerInfo() != null) {
            enterGameRsp.setPlayerAllInfoDB(playerAllInfoDB);
            MsgUtil.sendMsg(msg.getHeaderProto(), enterGameRsp);
        } else {
            MsgUtil.sendErrorMsg(msg.getHeaderProto(), 1);
        }
        return 1;
    }
}
