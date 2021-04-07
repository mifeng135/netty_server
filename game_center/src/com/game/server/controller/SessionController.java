package com.game.server.controller;

import core.annotation.Ctrl;
import core.annotation.CtrlCmd;
import core.msg.TransferMsg;
import core.util.ProtoUtil;
import io.netty.channel.ChannelHandlerContext;
import protocal.local.center.CenterSessionReq;

import static protocal.MsgConstant.*;

@Ctrl
public class SessionController {

    @CtrlCmd(cmd = MSG_CENTER_SESSION_REQ)
    public void openSession(TransferMsg msg, ChannelHandlerContext context) {
        CenterSessionReq centerSessionReq = ProtoUtil.deserializer(msg.getData(), CenterSessionReq.class);
    }
}
