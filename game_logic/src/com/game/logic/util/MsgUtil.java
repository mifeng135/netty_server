package com.game.logic.util;

import com.game.logic.PropertiesConfig;
import core.manager.LocalSocketManager;
import core.msg.TransferMsg;
import core.util.ProtoUtil;
import io.netty.channel.Channel;
import protocal.HeaderProto;


public class MsgUtil {

    public static void sendMsg(HeaderProto headerProto, Object msg) {
        byte[] data = ProtoUtil.serialize(msg);
        TransferMsg transferMsg = new TransferMsg();
        transferMsg.setHeaderProto(headerProto);
        transferMsg.setData(data);

        Channel channel = LocalSocketManager.getInstance().getChanel(PropertiesConfig.gateSocketIndex);
        if (channel != null && channel.isActive()) {
            channel.writeAndFlush(transferMsg);
        }
    }

    public static void sendMsg(HeaderProto headerProto, byte[] data) {
        TransferMsg transferMsg = new TransferMsg();
        transferMsg.setHeaderProto(headerProto);
        transferMsg.setData(data);

        Channel channel = LocalSocketManager.getInstance().getChanel(PropertiesConfig.gateSocketIndex);
        if (channel != null && channel.isActive()) {
            channel.writeAndFlush(transferMsg);
        }
    }
}
