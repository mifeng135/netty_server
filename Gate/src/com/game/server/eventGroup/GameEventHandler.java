package com.game.server.eventGroup;

import core.groupHelper.EventHandler;
import core.manager.WebSocketConnectionManager;
import core.proto.TransferMsg;
import core.util.SocketUtil;

/**
 * Created by Administrator on 2020/7/3.
 */
public class GameEventHandler implements EventHandler {

    @Override
    public void onEvent(TransferMsg transferMsg) {
        SocketUtil.sendWebSocketMsg(transferMsg.getPlayerIndex(), transferMsg.getMsgId(), transferMsg.getData());
    }
}
