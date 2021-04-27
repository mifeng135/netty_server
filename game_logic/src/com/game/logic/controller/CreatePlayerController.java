package com.game.logic.controller;

import com.game.logic.util.MsgUtil;
import core.annotation.Ctrl;
import core.annotation.CtrlCmd;
import core.msg.TransferMsg;
import core.netty.asyncHttp.AsyncHttp;
import core.util.ProtoUtil;
import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.Response;
import protocal.MsgConstant;
import protocal.local.db.player.PlayerAllInfoDB;
import protocal.remote.user.CreatePlayerRsp;

import static protocal.MsgConstant.*;

@Ctrl
public class CreatePlayerController extends AsyncCompletionHandler<Integer> {

    @CtrlCmd(cmd = MsgConstant.MSG_CREATE_PLAYER_REQ)
    public void createPlayer(TransferMsg msg) {
        msg.getHeaderProto().setMsgId(DB_CMD_CREATE_PLAYER_REQ);
        AsyncHttp.getInstance().postAsync(msg.getHeaderProto(), msg.getData(), this);
    }

    @Override
    public Integer onCompleted(Response response) throws Exception {
        TransferMsg msg = ProtoUtil.decodeDBHttpMsg(response.getResponseBodyAsBytes());
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
        return 1;
    }
}
