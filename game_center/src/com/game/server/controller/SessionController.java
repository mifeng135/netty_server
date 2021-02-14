package com.game.server.controller;

import com.game.server.manager.SessionManager;
import core.annotation.Ctrl;
import core.annotation.CtrlCmd;
import core.msg.TransferMsg;
import core.util.ProtoUtil;
import io.netty.channel.ChannelHandlerContext;
import protocol.local.center.CenterSessionReq;

import static core.Constants.SOCKET_OPEN;
import static protocol.MsgConstant.*;

@Ctrl
public class SessionController {

    @CtrlCmd(cmd = MSG_CENTER_SESSION_REQ)
    public void openSession(TransferMsg msg, ChannelHandlerContext context) {
        CenterSessionReq centerSessionReq = ProtoUtil.deserializer(msg.getData(), CenterSessionReq.class);
        int state = centerSessionReq.getState();
        if (state == SOCKET_OPEN) {
            SessionManager.addSession(centerSessionReq.getPlayerIndex());
        } else {
            SessionManager.removeSession(centerSessionReq.getPlayerIndex());
        }
    }
}
