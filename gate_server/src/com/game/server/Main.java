package com.game.server;

import com.game.server.schedule.SocketConnectCount;
import com.game.server.server.GateServer;
import com.game.server.serverConfig.ServerConfig;

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
