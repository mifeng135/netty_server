package com.game.server;


import core.annotation.CtrlAnnotation;
import core.group.EventThreadGroup;
import core.netty.asyncHttp.AsyncHttp;
import core.netty.tcp.TcpServer;


import static config.Config.*;
import static core.Constants.LOCAL;

public class SceneApplication {
    public static void main(String[] args) {
        AsyncHttp.getInstance();
        CtrlAnnotation.getInstance();
        new EventThreadGroup(Runtime.getRuntime().availableProcessors(), SceneApplication.class.getName());
        new TcpServer(SCENE_SERVER_IP, SCENE_SERVER_PORT, LOCAL).startServer();
    }
}
