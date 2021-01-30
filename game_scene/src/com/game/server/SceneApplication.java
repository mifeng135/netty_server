package com.game.server;


import core.group.EventThreadGroup;
import core.netty.tcp.TcpConnection;

import static com.game.server.Config.*;


public class SceneApplication {
    public static void main(String[] args) {
        new EventThreadGroup(LOGIC_THREAD_COUNT, LOGIC_THREAD_NAME);
        new TcpConnection(Config.CONNECT_SCENE_CENTER_SOCKET_INDEX).connect(Config.CONNECT_SCENE_CENTER_SERVER_IP, Config.CONNECT_SCENE_CENTER_SERVER_PORT);
    }
}
