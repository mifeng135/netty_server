package com.game.server.server;


import com.game.server.core.netty.NettyServer;
import com.game.server.core.netty.ServerHandler;
import com.game.server.eventGroup.db.DBEventGroup;
import com.game.server.eventGroup.db.DBEventHandler;
import com.game.server.eventGroup.game.GameEventGroup;
import com.game.server.eventGroup.game.GameEventHandler;
import com.game.server.socket.login.*;


/**
 * Created by Administrator on 2020/6/8.
 */
public class GateServer extends NettyServer {

    public GateServer(String ip, int port) {
        super(ip, port);
    }

    @Override
    public void init() {
        SendToLogin.getInstance().start();
        SendToDB.getInstance().start();
        SendToGame.getInstance().start();

        new DBEventGroup(DBEventHandler.class, 1);
        new ReceiveFromDB().start();

        new GameEventGroup(GameEventHandler.class, 1);
        new ReceiveFromGame().start();
    }

    @Override
    public ServerHandler getServerHandler() {
        return new GateServerHandler();
    }
}
