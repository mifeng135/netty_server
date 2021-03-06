package com.game.gate.controller;


import com.game.gate.util.TcpUtil;
import constants.MsgConstant;
import core.Constants;
import core.annotation.ctrl.Ctrl;
import core.annotation.ctrl.CtrlCmd;
import core.manager.LocalRouterSocketManager;
import core.manager.LocalSocketManager;
import core.manager.RemoteSocketManager;
import core.msg.TransferClientMsg;
import core.msg.TransferMsg;
import core.util.ProtoUtil;
import core.util.TimeUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import protocal.local.system.RegisterMsgCmdPush;
import protocal.push.NetDelayPush;
import protocal.remote.system.*;

import java.util.HashSet;
import java.util.Set;

import static constants.MsgConstant.*;
import static core.msg.SysMsgConstants.MSG_LOCAL_SOCKET_CLOSE_PUSH;
import static core.msg.SysMsgConstants.MSG_REGISTER_MSG_CMD_PUSH;
import static core.msg.SysMsgConstants.MSG_REMOTE_SOCKET_CLOSE_PUSH;

@Ctrl
public class BaseController {

    @CtrlCmd(cmd = MSG_REMOTE_OPEN_SOCKET_REQ)
    public void remoteSocketOpen(TransferMsg msg) {
        ClientSocketLoginReq clientSocketLoginReq = ProtoUtil.deserializer(msg.getData(), ClientSocketLoginReq.class);
        int playerIndex = clientSocketLoginReq.getPlayerIndex();
        process(msg.getContext(), playerIndex);
        TcpUtil.sendToClient(playerIndex, MsgConstant.MSG_REMOTE_OPEN_SOCKET_RSP, new ClientSocketLoginRsp());
        LocalRouterSocketManager.getInstance().sendRouterMsg(msg);
    }

    @CtrlCmd(cmd = MSG_HEART_BEAT_REQ)
    public void heartBeat(TransferMsg msg) {
        HeartBeatRsp heartBeatRsp = new HeartBeatRsp();
        heartBeatRsp.setTime(TimeUtil.getCurrentTimeSecond());
        TcpUtil.sendToClient(msg.getHeaderProto().getPlayerIndex(), MsgConstant.MSG_HEART_BEAT_RSP, heartBeatRsp);
    }

    @CtrlCmd(cmd = MSG_RECONNECT_REQ)
    public void reconnect(TransferMsg msg) {
        ReconnectReq reconnectReq = ProtoUtil.deserializer(msg.getData(), ReconnectReq.class);
        int socketIndex = reconnectReq.getSocketIndex();
        process(msg.getContext(), socketIndex);
        TcpUtil.sendToClient(socketIndex, MSG_RECONNECT_RSP, new ReconnectRsp());
    }

    @CtrlCmd(cmd = MSG_REMOTE_SOCKET_CLOSE_PUSH)
    public void remoteSocketClose(TransferMsg msg) {
        LocalRouterSocketManager.getInstance().sendRouterMsg(msg);
        RemoteSocketManager.getInstance().removeChannel(msg.getContext().channel());
    }

    @CtrlCmd(cmd = MSG_LOCAL_SOCKET_CLOSE_PUSH)
    public void localSocketClose(TransferMsg msg) {
        LocalSocketManager.getInstance().removeChannel(msg.getContext().channel());
    }

    @CtrlCmd(cmd = MSG_REGISTER_MSG_CMD_PUSH)
    public void localSocket(TransferMsg msg) {
        RegisterMsgCmdPush registerMsgCmdReq = ProtoUtil.deserializer(msg.getData(), RegisterMsgCmdPush.class);
        Set<Integer> set = new HashSet<>();
        set.addAll(registerMsgCmdReq.getMsgList());
        LocalRouterSocketManager.getInstance().addRouter(msg.getHeaderProto().getPlayerIndex(), set);
    }

    @CtrlCmd(cmd = MSG_NET_DELAY_PUSH)
    public void netDelay(TransferMsg msg) {
        NetDelayPush delayPush = ProtoUtil.deserializer(msg.getData(), NetDelayPush.class);
        msg.getContext().channel().attr(Constants.NET_DELAY).set(delayPush.getDelayTime());
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
        ReplaceAccountPush replaceRsq = new ReplaceAccountPush();
        byte[] data = ProtoUtil.serialize(replaceRsq);
        TransferClientMsg transferMsg = new TransferClientMsg();
        transferMsg.setMsgId(MSG_REPLACE_ACCOUNT_PUSH);
        transferMsg.setData(data);
        channel.writeAndFlush(transferMsg).addListener((ChannelFutureListener) channelFuture -> channelFuture.channel().close());
    }
}
