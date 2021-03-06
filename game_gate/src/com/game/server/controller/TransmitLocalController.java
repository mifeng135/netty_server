package com.game.server.controller;


import core.annotation.Ctrl;
import core.annotation.CtrlCmd;
import core.manager.LocalRouterSocketManager;
import core.msg.TransferMsg;
import io.netty.channel.ChannelHandlerContext;


import static protocol.MsgConstant.MSG_GATE_SEND_LOCAL;

@Ctrl
public class TransmitLocalController {

    @CtrlCmd(cmd = MSG_GATE_SEND_LOCAL)
    public void sendLocalServer(TransferMsg msg, ChannelHandlerContext context) {
        LocalRouterSocketManager.getInstance().sendRouterMsg(msg);
    }
}
