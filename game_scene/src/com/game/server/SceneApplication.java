package com.game.server;


import core.group.EventThreadGroup;
import core.netty.tcp.TcpConnection;

import static config.Config.*;

public class SceneApplication {
    public static void main(String[] args) {
        new EventThreadGroup(SCENE_LOGIC_THREAD_COUNT, SCENE_LOGIN_LOGIC_THREAD_NAME);
        new TcpConnection(SCENE_CENTER_SOCKET_INDEX).connect(CENTER_SERVER_IP, CENTER_SERVER_PORT);
    }
}
