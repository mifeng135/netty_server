package com.game.server.server;


import com.game.server.core.netty.NettyServer;
import com.game.server.core.netty.ServerHandler;
import com.game.server.eventGroup.db.DBEventGroup;
import com.game.server.eventGroup.db.DBEventHandler;
import com.game.server.socket.login.ReceiveFromDB;
import com.game.server.socket.login.SendToDB;
import com.game.server.socket.login.SendToLogin;


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

        new DBEventGroup(DBEventHandler.class,1);
        new ReceiveFromDB().start();
    }

    @Override
    public ServerHandler getServerHandler() {
        return new GateServerHandler();
    }
}
