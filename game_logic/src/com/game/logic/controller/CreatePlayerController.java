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
        TransferMsg httpMsg = ProtoUtil.decodeDBHttpMsg(response.getResponseBodyAsBytes());
        httpMsg.getHeaderProto().setMsgId(MSG_CREATE_PLAYER_RSP);
        MsgUtil.sendMsg(httpMsg.getHeaderProto(), httpMsg.getData());
        return 1;
    }
}
