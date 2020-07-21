package com.game.server.socket.game;

import com.game.server.core.zero.SocketSend;
import com.game.server.serverConfig.ServerConfig;

/**
 * Created by Administrator on 2020/7/3.
 */
public class SendToGame extends SocketSend{

    private static class DefaultInstance {
        static final SendToGame INSTANCE = new SendToGame();
    }

    public static SendToGame getInstance() {
        return DefaultInstance.INSTANCE;
    }

    private SendToGame() {}

    @Override
    protected String getSocketIp() {
        return ServerConfig.SEND_TO_GAME;
    }
}
