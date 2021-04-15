package com.game.server.controller;

import bean.player.PlayerBean;
import core.annotation.Ctrl;
import core.annotation.CtrlCmd;
import core.msg.TransferMsg;
import core.netty.asyncHttp.AsyncHttp;
import io.netty.channel.ChannelHandlerContext;
import protocal.MsgConstant;

@Ctrl
public class EnterGameController {

    @CtrlCmd(cmd = MsgConstant.MSG_ENTER_GAME_REQ)
    public void playerEnterGame(TransferMsg msg, ChannelHandlerContext context) {
        int playerIndex = msg.getHeaderProto().getPlayerIndex();
    }
}
