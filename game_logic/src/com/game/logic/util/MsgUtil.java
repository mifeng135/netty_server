package com.game.logic.util;

import com.game.logic.PropertiesConfig;
import core.manager.LocalSocketManager;
import core.msg.TransferMsg;
import core.util.ProtoUtil;
import io.netty.channel.Channel;
import core.msg.HeaderProto;
import protocal.remote.system.ErrorRsp;

import java.util.List;


public class MsgUtil {

    public static void sendMsg(HeaderProto headerProto, Object msg) {
        byte[] data = ProtoUtil.serialize(msg);
        executeSendMsg(headerProto, data);
    }

    public static void sendMsg(HeaderProto headerProto, byte[] data) {
        executeSendMsg(headerProto, data);
    }

    public static void sendErrorMsg(HeaderProto headerProto, byte[] data) {
        headerProto.setSuccess(false);
        executeSendMsg(headerProto, data);
    }

    public static void sendErrorMsg(HeaderProto headerProto, Object msg) {
        byte[] data = ProtoUtil.serialize(msg);
        headerProto.setSuccess(false);
        executeSendMsg(headerProto, data);
    }

    public static void sendErrorMsg(HeaderProto headerProto, int errorCode) {
        ErrorRsp errorRsp = new ErrorRsp();
        errorRsp.setErrorCode(errorCode);
        errorRsp.setMsgId(headerProto.getMsgId());
        headerProto.setSuccess(false);
        byte[] data = ProtoUtil.serialize(errorRsp);
        executeSendMsg(headerProto, data);
    }

    public static void sendMsgWithList(HeaderProto headerProto, Object msg, List<Integer> playerList) {
        if (playerList.size() <= 0) {
            return;
        }
        byte[] data = ProtoUtil.serialize(msg);
        headerProto.setNoticeList(playerList);
        executeSendMsg(headerProto, data);
    }

    public static void sendMsgWithList(HeaderProto headerProto, byte[] data, List<Integer> playerList) {
        if (playerList.size() <= 0) {
            return;
        }
        headerProto.setNoticeList(playerList);
        executeSendMsg(headerProto, data);
    }

    public static void sendBroadCast(HeaderProto headerProto, Object msg) {
        byte[] data = ProtoUtil.serialize(msg);
        headerProto.setBroadcast(true);
        executeSendMsg(headerProto, data);
    }

    public static void sendBroadCast(HeaderProto headerProto, byte[] data) {
        headerProto.setBroadcast(true);
        executeSendMsg(headerProto, data);
    }

    private static void executeSendMsg(HeaderProto headerProto, byte[] data) {
        TransferMsg transferMsg = new TransferMsg();
        transferMsg.setHeaderProto(headerProto);
        transferMsg.setData(data);

        Channel channel = LocalSocketManager.getInstance().getChanel(PropertiesConfig.gateLogicSocketIndex);
        if (channel != null && channel.isActive()) {
            channel.writeAndFlush(transferMsg);
        }
    }
}
