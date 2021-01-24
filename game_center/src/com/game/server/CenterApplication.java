package com.game.server;


import core.group.EventThreadGroup;
import core.netty.tcp.TcpServer;

public class CenterApplication {
    public static void main(String[] args) {
        new EventThreadGroup(Config.LOGIC_THREAD_COUNT);
        new TcpServer(Config.SERVER_IP, Config.SERVER_PORT);
    }
}
