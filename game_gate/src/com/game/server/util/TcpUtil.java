package com.game.server.util;

import core.manager.LocalSocketManager;
import core.manager.SocketManager;
import core.msg.TransferMsg;
import core.util.ProtoUtil;
import io.netty.channel.Channel;

import java.util.List;

import static com.game.server.Config.CONNECT_GATE_CENTER_SOCKET_INDEX;
import static core.Constants.MSG_RESULT_FAIL;
import static core.Constants.MSG_RESULT_SUCCESS;

public class TcpUtil {
    /**
     * send to center
     * @param playerIndex
     * @param msgId
     * @param msg
     */
    public static void sendToCenter(int playerIndex, int msgId, Object msg) {
        byte[] data = ProtoUtil.serialize(msg);

        TransferMsg transferMsg = new TransferMsg();
        transferMsg.setPlayerIndex(playerIndex);
        transferMsg.setMsgId(msgId);
        transferMsg.setData(data);

        Channel channel = LocalSocketManager.getInstance().getChanel(CONNECT_GATE_CENTER_SOCKET_INDEX);
        if (channel != null && channel.isActive()) {
            channel.writeAndFlush(transferMsg);
        }
    }

    /**
     * send to client error
     * @param playerIndex
     * @param msgId
     * @param msg
     */
    public static void sendToClientError(int playerIndex, int msgId, Object msg) {
        byte[] data = ProtoUtil.serialize(msg);
        TransferMsg transferMsg = new TransferMsg();
        transferMsg.setMsgId(msgId);
        transferMsg.setData(data);
        transferMsg.setResult(MSG_RESULT_SUCCESS);
        Channel channel = SocketManager.getInstance().getChanel(playerIndex);
        if (channel != null && channel.isActive()) {
            channel.writeAndFlush(transferMsg);
        }
    }

    /**
     * send msg to one client
     * @param playerIndex
     * @param msgId
     * @param msg
     */
    public static void sendToClient(int playerIndex, int msgId, Object msg) {
        byte[] data = ProtoUtil.serialize(msg);
        TransferMsg transferMsg = new TransferMsg();
        transferMsg.setMsgId(msgId);
        transferMsg.setData(data);
        transferMsg.setResult(MSG_RESULT_FAIL);
        Channel channel = SocketManager.getInstance().getChanel(playerIndex);
        if (channel != null && channel.isActive()) {
            channel.writeAndFlush(transferMsg);
        }
    }


    /**
     * send all client
     * @param msgId
     * @param msg
     */
    public static void sendAllClient(int msgId, Object msg) {
        byte[] data = ProtoUtil.serialize(msg);
        TransferMsg transferMsg = new TransferMsg();
        transferMsg.setMsgId(msgId);
        transferMsg.setData(data);
        transferMsg.setResult(MSG_RESULT_SUCCESS);
        SocketManager.getInstance().broadcast(transferMsg);
    }


    /**
     * send to client with client list
     * @param list playerIndex list
     * @param msgId
     * @param msg
     */
    public static void sendToClientWithList(List<Integer> list, int msgId, Object msg) {
        byte[] data = ProtoUtil.serialize(msg);
        TransferMsg transferMsg = new TransferMsg();
        transferMsg.setMsgId(msgId);
        transferMsg.setData(data);
        transferMsg.setResult(MSG_RESULT_SUCCESS);

        for (int i = 0; i < list.size(); i++) {
            int playerIndex = list.get(i);
            Channel channel = SocketManager.getInstance().getChanel(playerIndex);
            if (channel != null && channel.isActive()) {
                channel.writeAndFlush(transferMsg);
            }
        }
    }
}
