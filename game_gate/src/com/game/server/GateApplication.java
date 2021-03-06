package com.game.server;

import core.group.EventThreadGroup;
import core.netty.tcp.TcpConnection;
import core.netty.tcp.TcpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static config.Config.*;
import static core.Constants.REMOTE;


/**
 * Created by Administrator on 2020/6/1.
 */
public class GateApplication {

    private static Logger logger = LoggerFactory.getLogger(TcpServer.class);

    public static void main(String[] arg) {
        new EventThreadGroup(Runtime.getRuntime().availableProcessors(), CustomEventHandler.class, GateApplication.class.getName());
        new TcpServer(GATE_SERVER_IP, GATE_SERVER_PORT, REMOTE).startServer();
        new TcpConnection(GATE_CENTER_SOCKET_INDEX).connect(CENTER_SERVER_IP, CENTER_SERVER_PORT);
        new TcpConnection(GATE_SCENE_SOCKET_INDEX).connect(SCENE_SERVER_IP, SCENE_SERVER_PORT);
    }
}
