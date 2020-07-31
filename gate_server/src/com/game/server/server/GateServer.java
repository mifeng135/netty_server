package com.game.server.server;


import com.game.server.core.netty.NettyServer;
import com.game.server.core.netty.ServerHandler;
/**
 * Created by Administrator on 2020/6/8.
 */
public class GateServer extends NettyServer {

    private final ServerHandler serverHandler;

    public GateServer(String ip, int port) {
        super(ip, port);
        serverHandler = new GateServerHandler();
    }

    @Override
    public void init() {

    }

    @Override
    public ServerHandler getServerHandler() {
        return serverHandler;
    }
}
