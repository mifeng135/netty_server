package com.game.server;

import core.group.EventThreadGroup;
import core.netty.tcp.TcpConnection;
import core.netty.tcp.TcpServer;


/**
 * Created by Administrator on 2020/6/1.
 */
public class GateApplication {

    public static void main(String[] arg) {
        new EventThreadGroup(Config.LOGIC_THREAD_COUNT);
        new TcpServer(Config.SERVER_IP, Config.SERVER_PORT); //开启gate服务器
        new TcpConnection(Config.CONNECT_GATE_CENTER_SOCKET_INDEX).connect(Config.CONNECT_GATE_CENTER_SERVER_IP, Config.CONNECT_GATE_CENTER_SERVER_PORT);
    }
}
