package com.game.server.controller;


import com.game.server.util.TcpUtil;
import core.Constants;
import core.annotation.Ctrl;
import core.annotation.CtrlCmd;
import core.manager.LocalRouterSocketManager;
import core.manager.RemoteSocketManager;
import core.msg.TransferClientMsg;
import core.msg.TransferMsg;
import core.util.ProtoUtil;
import core.util.TimeUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import protocal.MsgConstant;
import protocal.local.system.TcpRsp;
import protocal.remote.system.*;

import java.util.HashSet;
import java.util.Set;

import static protocal.MsgConstant.*;

@Ctrl
public class BaseController {

    @CtrlCmd(cmd = MSG_SOCKET_LOGIN_REQ)
    public void socketLogin(TransferMsg msg, ChannelHandlerContext context) {
        LoginGateReq loginGateReq = ProtoUtil.deserializer(msg.getData(), LoginGateReq.class);
        int playerIndex = loginGateReq.getPlayerId();
        process(context, playerIndex);
        TcpUtil.sendToClient(playerIndex, MsgConstant.MSG_SOCKET_LOGIN_RSP, new LoginGateRsp());
        LocalRouterSocketManager.getInstance().sendRouterMsg(msg);
    }

    @CtrlCmd(cmd = MSG_HEART_BEAT_REQ)
    public void heartBeat(TransferMsg msg, ChannelHandlerContext context) {
        HeartBeartRsq heatbeartRsq = new HeartBeartRsq();
        heatbeartRsq.setTime(TimeUtil.getCurrentTimeSecond());
        TcpUtil.sendToClient(msg.getHeaderProto().getPlayerIndex(), MsgConstant.MSG_HEART_BEAT_RSP, heatbeartRsq);
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
        LocalRouterSocketManager.getInstance().sendRouterMsg(msg);
    }

    @CtrlCmd(cmd = MSG_LOCAL_SOCKET_RSP)
    public void localSocket(TransferMsg msg, ChannelHandlerContext context) {
        TcpRsp tcpRsp = ProtoUtil.deserializer(msg.getData(), TcpRsp.class);
        Set<Integer> set = new HashSet<>();
        set.addAll(tcpRsp.getMsgList());
        LocalRouterSocketManager.getInstance().addRouter(msg.getHeaderProto().getPlayerIndex(), set);
    }

    private void process(ChannelHandlerContext context, int playerIndex) {
        context.channel().attr(Constants.PLAYER_INDEX).setIfAbsent(playerIndex);
        Channel oldChannel = RemoteSocketManager.getInstance().getChanel(playerIndex);
        if (oldChannel != null) {
            RemoteSocketManager.getInstance().removeChannel(playerIndex);
            sendReplaceAccount(oldChannel);
        }
        RemoteSocketManager.getInstance().putChannel(playerIndex, context.channel());
    }

    private void sendReplaceAccount(Channel channel) {
        ReplaceRsq replaceRsq = new ReplaceRsq();
        byte[] data = ProtoUtil.serialize(replaceRsq);
        TransferClientMsg transferMsg = new TransferClientMsg();
        transferMsg.setMsgId(MSG_REPLACE_ACCOUNT_PUSH);
        transferMsg.setData(data);
        channel.writeAndFlush(transferMsg).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                channelFuture.channel().close();
            }
        });
    }
}
