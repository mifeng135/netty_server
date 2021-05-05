package com.game.db.util;

import com.game.db.PropertiesConfig;
import core.manager.LocalSocketManager;
import core.msg.HeaderProto;
import core.msg.TransferMsg;
import core.util.ProtoUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.*;

public class MsgUtil {

    public static void sendToLogic(TransferMsg transferMsg, Object msg) {
        byte[] data = ProtoUtil.serialize(msg);
        transferMsg.getHeaderProto().setAttackData(transferMsg.getData());
        executeSendMsg(transferMsg.getHeaderProto(), data);
    }

    public static void sendToLogic(TransferMsg transferMsg, byte[] msg) {
        transferMsg.getHeaderProto().setAttackData(transferMsg.getData());
        executeSendMsg(transferMsg.getHeaderProto(), msg);
    }

    private static void executeSendMsg(HeaderProto headerProto, byte[] data) {
        TransferMsg transferMsg = new TransferMsg();
        transferMsg.setHeaderProto(headerProto);
        transferMsg.setData(data);

        Channel channel = LocalSocketManager.getInstance().getChanel(PropertiesConfig.logicDBSocketIndex);
        if (channel != null && channel.isActive()) {
            channel.writeAndFlush(transferMsg);
        }
    }
}
