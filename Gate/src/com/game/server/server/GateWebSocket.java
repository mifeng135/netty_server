package com.game.server.server;

import core.netty.WebSocketHandler;
import core.netty.WebSocketServer;

/**
 * Created by Administrator on 2020/12/13.
 */
public class GateWebSocket  extends WebSocketServer {

    private final WebSocketHandler serverHandler;

    public GateWebSocket(String ip, int port) {
        super(ip, port);
        serverHandler = new GateServerHandler();
    }

    @Override
    public WebSocketHandler getServerHandler() {
        return serverHandler;
    }
}
