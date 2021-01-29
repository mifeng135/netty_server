package com.game.server;


import core.group.EventThreadGroup;
import core.netty.tcp.TcpConnection;


public class SceneApplication {
    public static void main(String[] args) {
        new EventThreadGroup(Config.LOGIC_THREAD_COUNT);
        new TcpConnection(Config.CONNECT_SCENE_CENTER_SOCKET_INDEX).connect(Config.CONNECT_SCENE_CENTER_SERVER_IP, Config.CONNECT_SCENE_CENTER_SERVER_PORT);
    }
}
