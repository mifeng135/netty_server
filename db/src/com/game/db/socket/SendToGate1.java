package com.game.db.socket;

import com.game.db.core.zero.SocketSend;
import com.game.db.serverConfig.ServerConfig;

/**
 * Created by Administrator on 2020/6/22.
 */
public class SendToGate1 extends SocketSend {


    private static class DefaultInstance {
        static final SendToGate1 INSTANCE = new SendToGate1();
    }

    public static SendToGate1 getInstance() {
        return DefaultInstance.INSTANCE;
    }


    private SendToGate1() {

    }

    @Override
    protected String getSocketIp() {
        return ServerConfig.SEND_TO_GATE_1;
    }
}
