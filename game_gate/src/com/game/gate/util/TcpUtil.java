package com.game.gate.util;

import core.manager.RemoteSocketManager;
import core.msg.TransferClientMsg;
import core.util.ProtoUtil;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import constants.MsgConstant;

import java.util.List;

import static core.Constants.MSG_RESULT_FAIL;
import static core.Constants.MSG_RESULT_SUCCESS;

public class TcpUtil {

    public static Logger logger = LoggerFactory.getLogger(TcpUtil.class);


    /**
     * send to client error
     *
     * @param playerIndex
     * @param msgId
     * @param data
     */
    public static void sendToClientError(int playerIndex, int msgId, byte[] data) {
        TransferClientMsg transferMsg = new TransferClientMsg();
        transferMsg.setData(data);
        transferMsg.setMsgId(msgId);
        transferMsg.setResult(MSG_RESULT_FAIL);
        Channel channel = RemoteSocketManager.getInstance().getChanel(playerIndex);
        if (channel != null && channel.isActive()) {
            channel.writeAndFlush(transferMsg);
        }
        logger.info("send error msg to client playerIndex = {} msgId = {}", playerIndex, msgId);
    }

    /**
     * send to client error
     *
     * @param playerIndex
     * @param msgId
     * @param msg
     */
    public static void sendToClientError(int playerIndex, int msgId, Object msg) {
        byte[] data = ProtoUtil.serialize(msg);
        TransferClientMsg transferMsg = new TransferClientMsg();
        transferMsg.setData(data);
        transferMsg.setMsgId(msgId);
        transferMsg.setResult(MSG_RESULT_FAIL);
        Channel channel = RemoteSocketManager.getInstance().getChanel(playerIndex);
        if (channel != null && channel.isActive()) {
            channel.writeAndFlush(transferMsg);
        }
        logger.info("send error msg to client playerIndex = {} msgId = {}", playerIndex, msgId);
    }


    /**
     * send msg to one client
     *
     * @param playerIndex
     * @param msgId
     * @param msg
     */
    public static void sendToClient(int playerIndex, int msgId, Object msg) {
        byte[] data = ProtoUtil.serialize(msg);
        TransferClientMsg transferMsg = new TransferClientMsg();
        transferMsg.setMsgId(msgId);
        transferMsg.setData(data);
        transferMsg.setResult(MSG_RESULT_SUCCESS);
        Channel channel = RemoteSocketManager.getInstance().getChanel(playerIndex);
        if (channel != null && channel.isActive()) {
            channel.writeAndFlush(transferMsg);
        }
        if (msgId != MsgConstant.MSG_HEART_BEAT_RSP) {
            logger.info("send msg to client playerIndex = {} msgId = {}", playerIndex, msgId);
        }
    }

    /**
     * send msg to one client
     *
     * @param playerIndex
     * @param msgId
     * @param data
     */
    public static void sendToClient(int playerIndex, int msgId, byte[] data) {
        TransferClientMsg transferMsg = new TransferClientMsg();
        transferMsg.setMsgId(msgId);
        transferMsg.setData(data);
        transferMsg.setResult(MSG_RESULT_SUCCESS);
        Channel channel = RemoteSocketManager.getInstance().getChanel(playerIndex);
        if (channel != null && channel.isActive()) {
            channel.writeAndFlush(transferMsg);
        }
        if (msgId != MsgConstant.MSG_HEART_BEAT_RSP) {
            logger.info("send msg to client playerIndex = {} msgId = {}", playerIndex, msgId);
        }
    }


    /**
     * send all client
     *
     * @param msgId
     * @param data
     */
    public static void sendAllClient(int msgId, byte[] data) {
        TransferClientMsg transferMsg = new TransferClientMsg();
        transferMsg.setMsgId(msgId);
        transferMsg.setData(data);
        transferMsg.setResult(MSG_RESULT_SUCCESS);
        RemoteSocketManager.getInstance().broadcast(transferMsg);
    }


    /**
     * send to client with client list
     *
     * @param list  playerIndex list
     * @param msgId
     * @param msg
     */
    public static void sendToClientWithList(List<Integer> list, int msgId, Object msg) {
        byte[] data = ProtoUtil.serialize(msg);
        TransferClientMsg transferMsg = new TransferClientMsg();
        transferMsg.setMsgId(msgId);
        transferMsg.setData(data);
        transferMsg.setResult(MSG_RESULT_SUCCESS);

        for (int i = 0; i < list.size(); i++) {
            int playerIndex = list.get(i);
            Channel channel = RemoteSocketManager.getInstance().getChanel(playerIndex);
            if (channel != null && channel.isActive()) {
                channel.writeAndFlush(transferMsg);
            }
        }
    }

    /**
     * send to client with client list
     *
     * @param list  playerIndex list
     * @param msgId
     * @param data
     */
    public static void sendToClientWithList(List<Integer> list, int msgId, byte[] data) {
        TransferClientMsg transferMsg = new TransferClientMsg();
        transferMsg.setMsgId(msgId);
        transferMsg.setData(data);
        transferMsg.setResult(MSG_RESULT_SUCCESS);

        for (int i = 0; i < list.size(); i++) {
            int playerIndex = list.get(i);
            Channel channel = RemoteSocketManager.getInstance().getChanel(playerIndex);
            if (channel != null && channel.isActive()) {
                channel.writeAndFlush(transferMsg);
            }
        }
    }
}
