package com.game.server.socket.login;

import com.game.server.core.zero.SocketReceive;
import com.game.server.serverConfig.ServerConfig;

/**
 * Created by Administrator on 2020/7/3.
 */
public class ReceiveFromGate extends SocketReceive {
    @Override
    protected String getSocketIp() {
        return ServerConfig.RECEIVE_FROM_GATE;
    }

    @Override
    protected String getRegionString(int cmd) {
        return null;
    }
}
