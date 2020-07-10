package com.game.server.socket.login;

import com.game.server.core.zero.SocketSend;
import com.game.server.serverConfig.ServerConfig;

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
        return ServerConfig.SEND_TO_DB_1;
    }
}
