package com.game.server;


import core.group.EventThreadGroup;
import core.netty.tcp.TcpConnection;
import core.netty.tcp.TcpServer;

import static config.Config.*;
import static core.Constants.LOCAL;

public class SceneApplication {
    public static void main(String[] args) {
        new EventThreadGroup(Runtime.getRuntime().availableProcessors(), SceneApplication.class.getName());
        new TcpConnection(SCENE_CENTER_SOCKET_INDEX).connect(CENTER_SERVER_IP, CENTER_SERVER_PORT);
        new TcpServer(SCENE_SERVER_IP, SCENE_SERVER_PORT, LOCAL).startServer();
    }
}
