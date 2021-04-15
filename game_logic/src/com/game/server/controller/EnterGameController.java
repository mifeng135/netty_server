package com.game.server.controller;

import core.annotation.Ctrl;
import core.annotation.CtrlCmd;
import core.msg.TransferMsg;
import core.netty.asyncHttp.AsyncHttp;
import io.netty.channel.ChannelHandlerContext;
import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.Response;
import protocal.MsgConstant;

import static protocal.MsgConstant.DB_CMD_QUERY_PLAYER_INFO;

@Ctrl
public class EnterGameController extends AsyncCompletionHandler<Integer> {

    @CtrlCmd(cmd = MsgConstant.MSG_ENTER_GAME_REQ)
    public void playerEnterGame(TransferMsg msg, ChannelHandlerContext context) {
        msg.getHeaderProto().setMsgId(DB_CMD_QUERY_PLAYER_INFO);
        AsyncHttp.getInstance().postAsync(msg.getHeaderProto(), msg.getData(), this);
    }

    @Override
    public Integer onCompleted(Response response) throws Exception {
        TransferMsg httpMsg = AsyncHttp.getInstance().transferData(response.getResponseBodyAsBytes());

        return null;
    }
}
