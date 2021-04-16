package com.game.gate;

import core.annotation.CtrlAnnotation;
import core.group.EventThreadGroup;
import core.netty.tcp.TcpConnection;
import core.netty.tcp.TcpServer;

import static config.Config.*;
import static core.Constants.REMOTE;


/**
 * Created by Administrator on 2020/6/1.
 */
public class GateApplication {

    public static void main(String[] arg) {
        CtrlAnnotation.getInstance().init(GateApplication.class.getPackage().getName());
        new EventThreadGroup(Runtime.getRuntime().availableProcessors(), CustomEventHandler.class, GateApplication.class.getName());
        new TcpServer(GATE_SERVER_IP, GATE_SERVER_PORT, REMOTE).startServer();
        new TcpConnection(GATE_CENTER_SOCKET_INDEX).connect(CENTER_SERVER_IP, CENTER_SERVER_PORT);
        new TcpConnection(GATE_SCENE_SOCKET_INDEX).connect(SCENE_SERVER_IP, SCENE_SERVER_PORT);
    }
}
