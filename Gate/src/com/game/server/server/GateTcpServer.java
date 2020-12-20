package com.game.server.server;

import core.netty.TcpHandler;
import core.netty.TcpServer;

/**
 * Created by Administrator on 2020/12/19.
 */
public class GateTcpServer extends TcpServer {

    private final TcpHandler serverHandler;

    GateTcpServer(String ip, int port) {
        super(ip, port);
        serverHandler = new GateTcpHandler();
    }

    @Override
    public TcpHandler getServerHandler() {
        return serverHandler;
    }
}
