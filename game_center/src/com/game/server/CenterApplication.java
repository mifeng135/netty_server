package com.game.server;


import core.group.EventThreadGroup;
import core.netty.tcp.TcpServer;

import static com.game.server.Config.LOGIC_THREAD_NAME;
import static core.Constants.LOCAL;

public class CenterApplication {
    public static void main(String[] args) {
        new EventThreadGroup(Config.LOGIC_THREAD_COUNT, LOGIC_THREAD_NAME);
        new TcpServer(Config.SERVER_IP, Config.SERVER_PORT, LOCAL);
    }
}
