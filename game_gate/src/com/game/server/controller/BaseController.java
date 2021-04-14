package com.game.server.controller;


import com.game.server.util.TcpUtil;
import core.Constants;
import core.annotation.Ctrl;
import core.annotation.CtrlCmd;
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
import protocal.MsgConstant;
import protocal.local.system.RegisterMsgCmdReq;
import protocal.remote.system.*;

import java.util.HashSet;
import java.util.Set;

import static protocal.MsgConstant.*;

@Ctrl
public class BaseController {

    @CtrlCmd(cmd = MSG_REMOTE_OPEN_SOCKET_REQ)
    public void remoteSocketOpen(TransferMsg msg, ChannelHandlerContext context) {
        ClientSocketLoginReq clientSocketLoginReq = ProtoUtil.deserializer(msg.getData(), ClientSocketLoginReq.class);
        int playerIndex = clientSocketLoginReq.getPlayerIndex();
        process(context, playerIndex);
        TcpUtil.sendToClient(playerIndex, MsgConstant.MSG_REMOTE_OPEN_SOCKET_RSP, new ClientSocketLoginRsp());
        LocalRouterSocketManager.getInstance().sendRouterMsg(msg);
    }

    @CtrlCmd(cmd = MSG_HEART_BEAT_REQ)
    public void heartBeat(TransferMsg msg, ChannelHandlerContext context) {
        HeartBeartRsp heatbeartRsq = new HeartBeartRsp();
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

    @CtrlCmd(cmd = MSG_REMOTE_SOCKET_CLOSE_PUSH)
    public void remoteSocketClose(TransferMsg msg, ChannelHandlerContext context) {
        LocalRouterSocketManager.getInstance().sendRouterMsg(msg);
        RemoteSocketManager.getInstance().removeChannel(context.channel());
    }

    @CtrlCmd(cmd = MSG_LOCAL_SOCKET_CLOSE_PUSH)
    public void localSocketClose(TransferMsg msg, ChannelHandlerContext context) {
        LocalSocketManager.getInstance().removeChannel(context.channel());
    }

    @CtrlCmd(cmd = MSG_REGISTER_MSG_CMD_PUSH)
    public void localSocket(TransferMsg msg, ChannelHandlerContext context) {
        RegisterMsgCmdReq registerMsgCmdReq = ProtoUtil.deserializer(msg.getData(), RegisterMsgCmdReq.class);
        Set<Integer> set = new HashSet<>();
        set.addAll(registerMsgCmdReq.getMsgList());
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
        ReplaceAccountRsp replaceRsq = new ReplaceAccountRsp();
        byte[] data = ProtoUtil.serialize(replaceRsq);
        TransferClientMsg transferMsg = new TransferClientMsg();
        transferMsg.setMsgId(MSG_REPLACE_ACCOUNT_PUSH);
        transferMsg.setData(data);
        channel.writeAndFlush(transferMsg).addListener((ChannelFutureListener) channelFuture -> channelFuture.channel().close());
    }
}
