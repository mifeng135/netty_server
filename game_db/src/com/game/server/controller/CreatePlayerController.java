package com.game.server.controller;

import core.annotation.Ctrl;
import core.annotation.CtrlCmd;
import core.msg.TransferMsg;
import io.netty.channel.ChannelHandlerContext;
import protocal.MsgConstant;

@Ctrl
public class CreatePlayerController {

    @CtrlCmd(cmd = MsgConstant.MSG_CREATE_PLAYER_REQ)
    public void createPlayer(TransferMsg msg, ChannelHandlerContext context) {

    }
}
