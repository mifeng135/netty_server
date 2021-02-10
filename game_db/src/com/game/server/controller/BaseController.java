package com.game.server.controller;

import core.Constants;
import core.annotation.Ctrl;
import core.annotation.CtrlCmd;
import core.manager.LocalSocketManager;
import core.msg.TransferMsg;
import core.util.ProtoUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import protocol.system.TcpReq;

import static core.Constants.IDLE_STATE_HANDLER;
import static protocol.MsgConstant.*;
import static protocol.MsgConstant.MSG_TCP_REQ;

@Ctrl
public class BaseController {

    @CtrlCmd(cmd = MSG_TCP_REQ)
    public void tcpReq(TransferMsg msg, ChannelHandlerContext context) {
        TcpReq tcpReq = ProtoUtil.deserializer(msg.getData(), TcpReq.class);
        int socketIndex = tcpReq.getPlayerIndex();
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
