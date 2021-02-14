package com.game.server.util;

import core.manager.LocalSocketManager;
import core.msg.TransferMsg;
import core.util.ProtoUtil;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protocol.local.base.BaseLocalProto;

import static config.Config.LOGIN_DB_SOCKET_INDEX;

public class TcpUtil {


    public static Logger logger = LoggerFactory.getLogger(TcpUtil.class);

    /**
     * send to center
     *
     * @param msgId
     * @param msg
     */
    public static void sendToLogin(int msgId, BaseLocalProto msg) {
        byte[] data = ProtoUtil.serialize(msg);

        TransferMsg transferMsg = new TransferMsg();
        transferMsg.setMsgId(msgId);
        transferMsg.setData(data);

        Channel channel = LocalSocketManager.getInstance().getChanel(LOGIN_DB_SOCKET_INDEX);
        if (channel != null && channel.isActive()) {
            channel.writeAndFlush(transferMsg);
        }

        logger.info("send msg to center playerIndex = {} msgId = {}", msg.getPlayerIndex(), msgId);
    }
}
