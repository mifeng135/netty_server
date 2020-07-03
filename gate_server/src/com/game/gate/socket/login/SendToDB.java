package com.game.gate.socket.login;

import com.game.gate.core.zero.SocketSend;
import com.game.gate.serverConfig.ServerConfig;

/**
 * Created by Administrator on 2020/6/27.
 */
public class SendToDB extends SocketSend {

    private static class DefaultInstance {
        static final SendToDB INSTANCE = new SendToDB();
    }

    public static SendToDB getInstance() {
        return DefaultInstance.INSTANCE;
    }

    private SendToDB() {

    }
    @Override
    protected String getSocketIp() {
        return ServerConfig.SEND_TO_GATE_1;
    }
}
