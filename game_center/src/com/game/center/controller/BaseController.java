package com.game.center.controller;

import com.game.center.util.TcpUtil;
import core.Constants;
import core.annotation.Ctrl;
import core.annotation.CtrlAnnotation;
import core.annotation.CtrlCmd;
import core.manager.LocalSocketManager;
import core.msg.TransferMsg;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import protocal.local.system.RegisterMsgCmdPush;

import static protocal.MsgConstant.*;

@Ctrl
public class BaseController {

    @CtrlCmd(cmd = MSG_LOCAL_OPEN_SOCKET_PUSH)
    public void localSocketOpen(TransferMsg msg, ChannelHandlerContext context) {
        int socketIndex = msg.getHeaderProto().getPlayerIndex();
        process(context, socketIndex);
        RegisterMsgCmdPush registerMsgCmdReq = new RegisterMsgCmdPush();
        msg.getHeaderProto().setMsgId(MSG_REGISTER_MSG_CMD_PUSH);
        registerMsgCmdReq.setMsgList(CtrlAnnotation.getInstance().getMsgList());
        TcpUtil.sendToGate(msg.getHeaderProto(), registerMsgCmdReq);
    }

    @CtrlCmd(cmd = MSG_LOCAL_SOCKET_CLOSE_PUSH)
    public void localSocketClose(TransferMsg msg, ChannelHandlerContext context) {
        LocalSocketManager.getInstance().removeChannel(context.channel());
    }

    @CtrlCmd(cmd = MSG_REMOTE_SOCKET_CLOSE_PUSH)
    public void remoteSocketClose(TransferMsg msg, ChannelHandlerContext context) {
        // TODO: 2021/4/14
        //need process socket close
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