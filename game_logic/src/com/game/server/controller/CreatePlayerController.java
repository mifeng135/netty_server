package com.game.server.controller;

import core.annotation.Ctrl;
import core.annotation.CtrlCmd;
import core.msg.TransferMsg;
import core.netty.asyncHttp.AsyncHttp;
import core.util.ProtoUtil;
import io.netty.channel.ChannelHandlerContext;
import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.Response;
import protocal.MsgConstant;
import protocal.remote.user.CreatePlayerRsp;

import static protocal.MsgConstant.DB_CMD_CREATE_PLAYER_REQ;

@Ctrl
public class CreatePlayerController {

    @CtrlCmd(cmd = MsgConstant.MSG_CREATE_PLAYER_REQ)
    public void createPlayer(TransferMsg msg, ChannelHandlerContext context) {
        msg.getHeaderProto().setMsgId(DB_CMD_CREATE_PLAYER_REQ);
        AsyncHttp.getInstance().postAsync(msg.getHeaderProto(), msg.getData(), new AsyncCompletionHandler<Integer>() {
            @Override
            public Integer onCompleted(Response response) throws Exception {
                TransferMsg httpMsg = AsyncHttp.getInstance().transferData(response.getResponseBodyAsBytes());
                CreatePlayerRsp playerRsp = new CreatePlayerRsp();
                return 1;
            }
        });
    }
}
