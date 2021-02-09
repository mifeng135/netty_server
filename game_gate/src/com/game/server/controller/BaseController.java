package com.game.server.controller;


import com.game.server.util.TcpUtil;
import core.Constants;
import core.annotation.Ctrl;
import core.annotation.CtrlCmd;
import core.manager.SocketManager;
import core.msg.TransferMsg;
import core.util.ProtoUtil;
import core.util.TimeUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import protocol.MsgConstant;
import protocol.system.*;

import static core.Constants.*;
import static protocol.MsgConstant.*;

@Ctrl
public class BaseController {

    @CtrlCmd(cmd = MSG_SOCKET_INDEX_REQ)
    public void socketLogin(TransferMsg msg, ChannelHandlerContext context) {
        LoginGateReq loginGateReq = ProtoUtil.deserializer(msg.getData(), LoginGateReq.class);
        int playerIndex = loginGateReq.getPlayerId();
        process(context, playerIndex);
        TcpUtil.sendToClient(playerIndex, MsgConstant.MSG_SOCKET_INDEX_RSP, new LoginGateRsp());

        CenterSessionReq centerSessionReq = new CenterSessionReq();
        centerSessionReq.setState(SOCKET_OPEN);
        TcpUtil.sendToCenter(playerIndex, MSG_CENTER_SESSION_REQ, centerSessionReq);
    }

    @CtrlCmd(cmd = MSG_HEART_BEAT_REQ)
    public void heartBeat(TransferMsg msg, ChannelHandlerContext context) {
        HeartBeartRsq heatbeartRsq = new HeartBeartRsq();
        heatbeartRsq.setTime(TimeUtil.getCurrentTimeSecond());
        TcpUtil.sendToClient(msg.getPlayerIndex(), MsgConstant.MSG_HEART_BEAT_RSP, heatbeartRsq);
    }

    @CtrlCmd(cmd = MSG_RECONNECT_REQ)
    public void reconnect(TransferMsg msg, ChannelHandlerContext context) {
        ReconnectReq reconnectReq = ProtoUtil.deserializer(msg.getData(), ReconnectReq.class);
        int socketIndex = reconnectReq.getSocketIndex();
        process(context, socketIndex);
        TcpUtil.sendToClient(socketIndex, MSG_RECONNECT_RSP, new ReconnectRsp());
    }

    @CtrlCmd(cmd = MSG_CLOSE_SOCKET_REQ)
    public void socketClose(TransferMsg msg, ChannelHandlerContext context) {
        CenterSessionReq centerSessionReq = new CenterSessionReq();
        centerSessionReq.setState(SOCKET_CLOSE);
        TcpUtil.sendToCenter(msg.getPlayerIndex(), MSG_CENTER_SESSION_REQ, centerSessionReq);
    }

    private void process(ChannelHandlerContext context, int playerIndex) {
        context.channel().attr(Constants.PLAYER_INDEX).setIfAbsent(playerIndex);
        Channel oldChannel = SocketManager.getInstance().getChanel(playerIndex);
        if (oldChannel != null) {
            SocketManager.getInstance().removeChannel(playerIndex);
            sendReplaceAccount(oldChannel, playerIndex);
        }
        SocketManager.getInstance().putChannel(playerIndex, context.channel());
    }

    private void sendReplaceAccount(Channel channel, int playerIndex) {
        ReplaceRsq replaceRsq = new ReplaceRsq();
        byte[] data = ProtoUtil.serialize(replaceRsq);
        TransferMsg transferMsg = new TransferMsg();
        transferMsg.setPlayerIndex(playerIndex);
        transferMsg.setMsgId(MSG_REPLACE_ACCOUNT_RSP);
        transferMsg.setData(data);
        channel.writeAndFlush(transferMsg);
    }
}
