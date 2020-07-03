package com.game.server.socket;

import com.game.server.core.zero.SocketSend;
import com.game.server.serverConfig.ServerConfig;

/**
 * Created by Administrator on 2020/7/3.
 */
public class SendToGate extends SocketSend {

    private static class DefaultInstance {
        static final SendToGate INSTANCE = new SendToGate();
    }

    public static SendToGate getInstance() {
        return DefaultInstance.INSTANCE;
    }


    private SendToGate() {

    }
    @Override
    protected String getSocketIp() {
        return ServerConfig.SEND_TO_GATE;
    }
}
