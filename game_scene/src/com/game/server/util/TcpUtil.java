package com.game.server.util;

import core.manager.LocalSocketManager;
import core.msg.TransferMsg;
import core.util.ProtoUtil;
import io.netty.channel.Channel;
import protocol.local.base.HeaderProto;

import static config.Config.*;


public class TcpUtil {

    public static void sendToGate(HeaderProto headerProto, Object msg) {
        byte[] data = ProtoUtil.serialize(msg);
        TransferMsg transferMsg = new TransferMsg();
        transferMsg.setHeaderProto(headerProto);
        transferMsg.setData(data);

        Channel channel = LocalSocketManager.getInstance().getChanel(GATE_SCENE_SOCKET_INDEX);
        if (channel != null && channel.isActive()) {
            channel.writeAndFlush(transferMsg);
        }
    }
}
