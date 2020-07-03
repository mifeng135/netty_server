package com.game.server.socket.login;

import com.game.server.core.zero.SocketSend;
import com.game.server.serverConfig.ServerConfig;

/**
 * Created by Administrator on 2020/6/18.
 */
public class SendToLogin extends SocketSend {

    private static class DefaultInstance {
        static final SendToLogin INSTANCE = new SendToLogin();
    }

    public static SendToLogin getInstance() {
        return DefaultInstance.INSTANCE;
    }

    private SendToLogin() {

    }
    @Override
    protected String getSocketIp() {
        return ServerConfig.SERVER_LOGIN_IP;
    }
}
