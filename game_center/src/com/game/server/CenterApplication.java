package com.game.server;


import core.group.EventThreadGroup;
import core.netty.tcp.TcpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static config.Config.*;
import static core.Constants.LOCAL;

public class CenterApplication {
    private static Logger logger = LoggerFactory.getLogger(TcpServer.class);

    public static void main(String[] args) {
        new EventThreadGroup(CENTER_LOGIC_THREAD_COUNT, CENTER__LOGIC_THREAD_NAME);
        TcpServer server = new TcpServer(CENTER_SERVER_IP, CENTER_SERVER_PORT, LOCAL);
        server.setStartListener(channelFuture -> {
            if (channelFuture.isSuccess()) {
                logger.info("start center server success");
            }
        });
        server.startServer();
    }
}
