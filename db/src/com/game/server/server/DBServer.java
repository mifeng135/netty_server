package com.game.server.server;

import com.game.server.adapter.DBAdapter;
import com.game.server.core.annotation.CtrlAnnotation;
import com.game.server.core.annotation.SqlAnnotation;
import com.game.server.core.config.ServerInfo;
import com.game.server.core.manager.ReceiveSocketManager;
import com.game.server.core.manager.SendSocketManager;
import com.game.server.core.redis.RedisManager;
import com.game.server.core.sql.MysqlBatchHandle;
import com.game.server.core.zero.Receive;
import com.game.server.core.zero.Send;
import com.game.server.eventGroup.DBEventGroup;
import com.game.server.eventGroup.DBEventHandler;
import com.game.server.serverConfig.ServerConfig;

/**
 * Created by Administrator on 2020/6/27.
 */
public class DBServer {

    public void start() {


        for (int i = 0; i < ServerConfig.SEND_SERVER_LIST.size(); i++) {
            ServerInfo serverInfo = ServerConfig.SEND_SERVER_LIST.get(i);
            Send sendSocket = new Send(serverInfo.getIp(), serverInfo.getPort());
            sendSocket.start();
            SendSocketManager.getInstance().putSocket(serverInfo.getServerKey(), sendSocket);
        }

        for (int i = 0; i < ServerConfig.RECEIVE_SERVER_LIST.size(); i++) {
            ServerInfo serverInfo = ServerConfig.RECEIVE_SERVER_LIST.get(i);
            Receive receiveSocket = new Receive(serverInfo.getIp(), serverInfo.getPort(), serverInfo.getServerKey(), new DBAdapter());
            receiveSocket.start();
            ReceiveSocketManager.getInstance().putSocket(serverInfo.getServerKey(), receiveSocket);
        }

        RedisManager.getInstance();
        CtrlAnnotation.getInstance();
        SqlAnnotation.getInstance();
        MysqlBatchHandle.getInstance();

        new DBEventGroup(DBEventHandler.class, 2);

        System.out.println("db start success");
    }
}
