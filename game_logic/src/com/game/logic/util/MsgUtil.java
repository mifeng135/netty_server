package com.game.logic.util;

import com.game.logic.PropertiesConfig;
import core.manager.LocalSocketManager;
import core.msg.TransferMsg;
import core.util.ProtoUtil;
import io.netty.channel.Channel;
import protocal.HeaderProto;
import protocal.remote.system.ErroRsp;

import java.util.List;


public class MsgUtil {

    public static void sendMsg(HeaderProto headerProto, Object msg) {
        byte[] data = ProtoUtil.serialize(msg);
        excuteSendMsg(headerProto, data);
    }

    public static void sendMsg(HeaderProto headerProto, byte[] data) {
        excuteSendMsg(headerProto, data);
    }

    public static void sendErrorMsg(HeaderProto headerProto, byte[] data) {
        headerProto.setSuccess(false);
        excuteSendMsg(headerProto, data);
    }

    public static void sendErrorMsg(HeaderProto headerProto, Object msg) {
        byte[] data = ProtoUtil.serialize(msg);
        headerProto.setSuccess(false);
        excuteSendMsg(headerProto, data);
    }

    public static void sendErrorMsg(HeaderProto headerProto, int errorCode) {
        ErroRsp erroRsp = new ErroRsp();
        erroRsp.setErrorCode(errorCode);
        headerProto.setSuccess(false);
        byte[] data = ProtoUtil.serialize(erroRsp);
        excuteSendMsg(headerProto, data);
    }

    public static void sendMsgWithList(HeaderProto headerProto, Object msg, List<Integer> playerList) {
        byte[] data = ProtoUtil.serialize(msg);
        headerProto.setNoticeList(playerList);
        excuteSendMsg(headerProto, data);
    }

    public static void sendMsgWithList(HeaderProto headerProto, byte[] data, List<Integer> playerList) {
        headerProto.setNoticeList(playerList);
        excuteSendMsg(headerProto, data);
    }

    public static void sendBroadCast(HeaderProto headerProto, Object msg) {
        byte[] data = ProtoUtil.serialize(msg);
        headerProto.setBroadcast(true);
        excuteSendMsg(headerProto, data);
    }

    public static void sendBroadCast(HeaderProto headerProto, byte[] data) {
        headerProto.setBroadcast(true);
        excuteSendMsg(headerProto, data);
    }

    private static void excuteSendMsg(HeaderProto headerProto, byte[] data) {
        TransferMsg transferMsg = new TransferMsg();
        transferMsg.setHeaderProto(headerProto);
        transferMsg.setData(data);

        Channel channel = LocalSocketManager.getInstance().getChanel(PropertiesConfig.gateLogicSocketIndex);
        if (channel != null && channel.isActive()) {
            channel.writeAndFlush(transferMsg);
        }
    }
}
