package com.game.logic.controller;

import com.game.logic.util.MsgUtil;
import core.Constants;
import core.annotation.CA;
import core.annotation.Ctrl;
import core.annotation.CtrlCmd;
import core.manager.LocalSocketManager;
import core.msg.TransferMsg;
import io.netty.channel.ChannelHandlerContext;
import protocal.local.system.RegisterMsgCmdPush;

import static core.msg.SysMsgConstants.*;


@Ctrl
public class BaseController {

    @CtrlCmd(cmd = MSG_LOCAL_OPEN_SOCKET_PUSH)
    public void localSocketOpen(TransferMsg msg) {
        int socketIndex = msg.getHeaderProto().getPlayerIndex();
        process(msg.getContext(), socketIndex);
        RegisterMsgCmdPush registerMsgCmdReq = new RegisterMsgCmdPush();
        msg.getHeaderProto().setMsgId(MSG_REGISTER_MSG_CMD_PUSH);
        registerMsgCmdReq.setMsgList(CA.getInstance().getMsgList());
        MsgUtil.sendMsg(msg.getHeaderProto(), registerMsgCmdReq);
    }

    @CtrlCmd(cmd = MSG_REMOTE_SOCKET_CLOSE_PUSH)
    public void remoteSocketClose(TransferMsg msg) {
        // TODO: 2021/4/14
        //need process socket close
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
