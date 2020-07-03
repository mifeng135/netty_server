package com.game.game.socket.login;

import com.game.game.core.zero.SocketSend;
import com.game.game.serverConfig.ServerConfig;

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
