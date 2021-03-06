package com.game.server.controller;


import com.game.server.util.TcpUtil;
import core.annotation.Ctrl;
import core.annotation.CtrlCmd;
import core.msg.TransferMsg;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

import static protocol.MsgConstant.MSG_GATE_SEND_REMOTE;

@Ctrl
public class TransmitRemoteController {

    @CtrlCmd(cmd = MSG_GATE_SEND_REMOTE)
    public void sendRemoteClient(TransferMsg msg, ChannelHandlerContext context) {
        List noticeList = msg.getHeaderProto().getNoticeList();
        int msgId = msg.getHeaderProto().getMsgId();
        if (noticeList != null) {
            TcpUtil.sendToClientWithList(noticeList, msgId, msg.getData());
        } else if (msg.getHeaderProto().isBroadcast()) {
            TcpUtil.sendAllClient(msgId, msg.getData());
        } else {
            if (msg.getHeaderProto().isResult()) {
                TcpUtil.sendToClient(msg.getHeaderProto().getPlayerIndex(), msgId, msg.getData());
            } else {
                TcpUtil.sendToClientError(msg.getHeaderProto().getPlayerIndex(), msgId, msg.getData());
            }
        }
    }
}
