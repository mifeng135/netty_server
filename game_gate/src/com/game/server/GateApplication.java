package com.game.server;

import core.group.EventThreadGroup;
import core.netty.tcp.TcpConnection;
import core.netty.tcp.TcpServer;

import static com.game.server.Config.LOGIC_THREAD_NAME;
import static core.Constants.REMOTE;


/**
 * Created by Administrator on 2020/6/1.
 */
public class GateApplication {

    public static void main(String[] arg) {
        new EventThreadGroup(Config.LOGIC_THREAD_COUNT, LOGIC_THREAD_NAME);
        new TcpServer(Config.SERVER_IP, Config.SERVER_PORT, REMOTE);
        new TcpConnection(Config.CONNECT_GATE_CENTER_SOCKET_INDEX).connect(Config.CONNECT_GATE_CENTER_SERVER_IP, Config.CONNECT_GATE_CENTER_SERVER_PORT);
    }
}
