package com.game.server.controller;

import core.Constants;
import core.annotation.Ctrl;
import core.annotation.CtrlCmd;
import core.manager.LocalSocketManager;
import core.msg.TransferMsg;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import static core.Constants.IDLE_STATE_HANDLER;
import static protocal.MsgConstant.*;

@Ctrl
public class BaseController {

    @CtrlCmd(cmd = MSG_LOCAL_SOCKET_REQ)
    public void tcpReq(TransferMsg msg, ChannelHandlerContext context) {
        int socketIndex = msg.getHeaderProto().getPlayerIndex();
        process(context, socketIndex);
        context.channel().pipeline().remove(IDLE_STATE_HANDLER);

    }

    private void process(ChannelHandlerContext context, int socketIndex) {
        context.channel().attr(Constants.PLAYER_INDEX).setIfAbsent(socketIndex);
        Channel oldChannel = LocalSocketManager.getInstance().getChanel(socketIndex);
        if (oldChannel != null) {
            LocalSocketManager.getInstance().removeChannel(socketIndex);
        }
        LocalSocketManager.getInstance().putChannel(socketIndex, context.channel());
    }
}
