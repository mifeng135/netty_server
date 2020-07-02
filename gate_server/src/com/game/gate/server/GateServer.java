package com.game.gate.server;


import com.game.gate.core.groupHelper.EventHandler;
import com.game.gate.core.netty.NettyServer;
import com.game.gate.core.netty.ServerHandler;
import com.game.gate.eventGroup.db.DBEventGroup;
import com.game.gate.eventGroup.db.DBEventHandler;
import com.game.gate.socket.login.ReceiveFromDB;
import com.game.gate.socket.login.SendToDB;
import com.game.gate.socket.login.SendToLogin;


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
