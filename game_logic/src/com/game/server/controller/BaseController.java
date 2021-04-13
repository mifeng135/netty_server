package com.game.server.controller;

import com.game.server.util.TcpUtil;
import core.Constants;
import core.annotation.Ctrl;
import core.annotation.CtrlAnnotation;
import core.annotation.CtrlCmd;
import core.manager.LocalSocketManager;
import core.msg.TransferMsg;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import protocal.local.system.TcpRsp;

import static core.Constants.IDLE_STATE_HANDLER;
import static protocal.MsgConstant.MSG_CLOSE_SOCKET_REQ;
import static protocal.MsgConstant.MSG_LOCAL_SOCKET_REQ;
import static protocal.MsgConstant.MSG_LOCAL_SOCKET_RSP;

@Ctrl
public class BaseController {

    @CtrlCmd(cmd = MSG_LOCAL_SOCKET_REQ)
    public void tcpReq(TransferMsg msg, ChannelHandlerContext context) {
        int socketIndex = msg.getHeaderProto().getPlayerIndex();
        process(context, socketIndex);
        TcpRsp tcpRsp = new TcpRsp();
        tcpRsp.setMsgList(CtrlAnnotation.getInstance().getMsgList());
        msg.getHeaderProto().setMsgId(MSG_LOCAL_SOCKET_RSP);
        TcpUtil.sendToGate(msg.getHeaderProto(), tcpRsp);
    }

    @CtrlCmd(cmd = MSG_CLOSE_SOCKET_REQ)
    public void socketClose(TransferMsg msg, ChannelHandlerContext context) {

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