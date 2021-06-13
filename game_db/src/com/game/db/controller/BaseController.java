package com.game.db.controller;

import core.Constants;
import core.annotation.ctrl.Ctrl;
import core.annotation.ctrl.CtrlCmd;
import core.manager.LocalSocketManager;
import core.msg.TransferMsg;
import io.netty.channel.ChannelHandlerContext;

import static core.msg.SysMsgConstants.*;

@Ctrl
public class BaseController {


    @CtrlCmd(cmd = MSG_LOCAL_OPEN_SOCKET_PUSH)
    public void localSocketOpen(TransferMsg msg) {
        int socketIndex = msg.getHeaderProto().getPlayerIndex();
        process(msg.getContext(), socketIndex);
    }

    @CtrlCmd(cmd = MSG_LOCAL_SOCKET_CLOSE_PUSH)
    public void socketClose(TransferMsg msg) {
        LocalSocketManager.getInstance().removeChannel(msg.getContext().channel());
    }

    private void process(ChannelHandlerContext context, int socketIndex) {
        context.channel().attr(Constants.PLAYER_INDEX).setIfAbsent(socketIndex);
        LocalSocketManager.getInstance().putChannel(socketIndex, context.channel());
    }
}
