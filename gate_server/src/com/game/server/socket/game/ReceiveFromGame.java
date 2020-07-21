package com.game.server.socket.game;

import com.game.server.constant.MsgRegionConstant;
import com.game.server.core.zero.SocketReceive;
import com.game.server.serverConfig.ServerConfig;

/**
 * Created by Administrator on 2020/7/3.
 */
public class ReceiveFromGame extends SocketReceive{

    @Override
    protected String getSocketIp() {
        return ServerConfig.RECEIVE_FROM_GAME;
    }

    @Override
    protected String getRegionString(int cmd) {
        return MsgRegionConstant.MSG_REGION_GAME;
    }
}
