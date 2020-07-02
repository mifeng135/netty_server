package com.game.gate;

import com.game.gate.schedule.SocketConnectCount;
import com.game.gate.server.GateServer;
import com.game.gate.serverConfig.ServerConfig;

/**
 * Created by Administrator on 2020/6/1.
 */
public class Main {

    public static void main(String[] arg) {
        GateServer gateServer = new GateServer(ServerConfig.SERVER_IP, ServerConfig.SERVER_PORT);
        gateServer.doInitNetty();
        new SocketConnectCount();
    }
}
