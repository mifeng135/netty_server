package com.game.server;

import com.game.server.adapter.DBAdapter;
import com.game.server.adapter.GameAdapter;
import com.game.server.core.config.ServerInfo;
import com.game.server.core.manager.ReceiveSocketManager;
import com.game.server.core.manager.SendSocketManager;
import com.game.server.core.zero.Receive;
import com.game.server.core.zero.Send;
import com.game.server.eventGroup.db.DBEventGroup;
import com.game.server.eventGroup.db.DBEventHandler;
import com.game.server.eventGroup.game.GameEventGroup;
import com.game.server.eventGroup.game.GameEventHandler;
import com.game.server.schedule.SocketConnectCount;
import com.game.server.server.GateServer;
import com.game.server.serverConfig.ServerConfig;

/**
 * Created by Administrator on 2020/6/1.
 */
public class Main {

    public static void main(String[] arg) {

        for (int i = 0; i < ServerConfig.SEND_SERVER_LIST.size(); i++) {
            ServerInfo serverInfo = ServerConfig.SEND_SERVER_LIST.get(i);
            Send sendSocket = new Send(serverInfo.getIp(), serverInfo.getPort());
            sendSocket.start();
            SendSocketManager.getInstance().putSocket(serverInfo.getServerKey(), sendSocket);
        }

        for (int i = 0; i < ServerConfig.RECEIVE_SERVER_LIST.size(); i++) {
            ServerInfo serverInfo = ServerConfig.RECEIVE_SERVER_LIST.get(i);
            Receive receiveSocket = new Receive(serverInfo.getIp(), serverInfo.getPort(), serverInfo.getServerKey(), serverInfo.getAdapter());
            receiveSocket.start();
            ReceiveSocketManager.getInstance().putSocket(serverInfo.getServerKey(), receiveSocket);
        }
        new DBEventGroup(DBEventHandler.class, 1);
        new GameEventGroup(GameEventHandler.class, 1);

        GateServer gateServer = new GateServer(ServerConfig.SERVER_IP, ServerConfig.SERVER_PORT);
        gateServer.doInitNetty();
        new SocketConnectCount();
    }
}
