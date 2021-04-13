package com.game.server.controller;

import core.annotation.Ctrl;
import core.annotation.CtrlCmd;
import core.msg.TransferMsg;
import io.netty.channel.ChannelHandlerContext;

import static protocal.MsgConstant.*;

@Ctrl
public class PlayerEnterSceneController {

    @CtrlCmd(cmd = MSG_CLIENT_SOCKET_LOGIN_REQ)
    public void playerEnterScene(TransferMsg msg, ChannelHandlerContext context) {

    }
}
