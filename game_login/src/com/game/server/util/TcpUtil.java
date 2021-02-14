package com.game.server.util;

import core.manager.LocalSocketManager;
import core.msg.TransferMsg;
import core.util.ProtoUtil;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static config.Config.*;

public class TcpUtil {

    public static Logger logger = LoggerFactory.getLogger(TcpUtil.class);

    public static void sendToDB(int playerIndex, int msgId, Object msg) {
        byte[] data = ProtoUtil.serialize(msg);
        TransferMsg transferMsg = new TransferMsg();
        transferMsg.setPlayerIndex(playerIndex);
        transferMsg.setMsgId(msgId);
        transferMsg.setData(data);
        Channel channel = LocalSocketManager.getInstance().getChanel(LOGIN_DB_SOCKET_INDEX);
        if (channel != null && channel.isActive()) {
            channel.writeAndFlush(transferMsg);
        }
        logger.info("send msg to db playerIndex = {} msgId = {}", playerIndex, msgId);
    }
}
