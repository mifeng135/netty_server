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
        new EventThreadGroup(GATE_LOGIC_THREAD_COUNT, GATE_LOGIC_THREAD_NAME);
        TcpServer server = new TcpServer(GATE_SERVER_IP, GATE_SERVER_PORT, REMOTE);
        server.setStartListener(channelFuture -> {
            if (channelFuture.isSuccess()) {
                logger.info("start gate server success");
            }
        });
        server.startServer();
        new TcpConnection(GATE_CENTER_SOCKET_INDEX).connect(CENTER_SERVER_IP, CENTER_SERVER_PORT);
    }
}
