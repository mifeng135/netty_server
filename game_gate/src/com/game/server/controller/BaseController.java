package com.game.server.controller;


import com.game.server.Config;
import core.Constants;
import core.annotation.Ctrl;
import core.annotation.CtrlCmd;
import core.manager.SocketManager;
import core.msg.TransferMsg;
import core.util.ProtoUtil;
import core.util.SocketUtil;
import core.util.TimeUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import nanomsg.Socket;
import protocol.MsgConstant;
import protocol.system.*;

import static core.Constants.IDLE_STATE_HANDLER;
import static core.Constants.MSG_RESULT_SUCCESS;
import static protocol.MsgConstant.*;

@Ctrl
public class BaseController {

    @CtrlCmd(cmd = MSG_SOCKET_INDEX_REQ)
    public void socketLogin(TransferMsg msg, ChannelHandlerContext context) {
        LoginGateReq loginGateReq = ProtoUtil.deserializer(msg.getData(), LoginGateReq.class);
        int socketIndex = loginGateReq.getPlayerId();
        process(context, socketIndex);
        SocketUtil.sendRemoteTcpMsgToConnection(socketIndex, MsgConstant.MSG_SOCKET_INDEX_RSP, MSG_RESULT_SUCCESS, new LoginGateRsp());

        CenterSessionReq centerSessionReq = new CenterSessionReq();
        centerSessionReq.setSocketIndex(socketIndex);
        SocketUtil.sendLoaclTcpMsgToServer(Config.CONNECT_GATE_CENTER_SERVER_KEY, MSG_CENTER_SESSION_REQ, centerSessionReq);
    }

    @CtrlCmd(cmd = MSG_HEART_BEAT_REQ)
    public void heartBeat(TransferMsg msg, ChannelHandlerContext context) {
        HeartBeartRsq heatbeartRsq = new HeartBeartRsq();
        heatbeartRsq.setTime(TimeUtil.getCurrentTimeSecond());
        SocketUtil.sendRemoteTcpMsgToConnection(msg.getSocketIndex(), MsgConstant.MSG_HEART_BEAT_RSP, MSG_RESULT_SUCCESS, heatbeartRsq);
    }

    @CtrlCmd(cmd = MSG_RECONNECT_REQ)
    public void reconnect(TransferMsg msg, ChannelHandlerContext context) {
        ReconnectReq reconnectReq = ProtoUtil.deserializer(msg.getData(), ReconnectReq.class);
        int socketIndex = reconnectReq.getSocketIndex();
        process(context, socketIndex);
        SocketUtil.sendRemoteTcpMsgToConnection(socketIndex, MSG_RECONNECT_RSP, MSG_RESULT_SUCCESS, new ReconnectRsp());
    }

    private void process(ChannelHandlerContext context, int socketIndex) {
        context.channel().attr(Constants.SOCKET_INDEX).setIfAbsent(socketIndex);
        Channel oldChannel = SocketManager.getInstance().getChanel(socketIndex);
        if (oldChannel != null) {
            SocketManager.getInstance().removeChannel(socketIndex);
        }
        SocketManager.getInstance().putChannel(socketIndex, context.channel());
    }
}
