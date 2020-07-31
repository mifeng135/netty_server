package com.game.server.server;

import com.game.server.constant.MsgRegionConstant;
import com.game.server.core.config.ServerInfo;
import com.game.server.core.groupHelper.EventThreadGroup;
import com.game.server.core.manager.ReceiveSocketManager;
import com.game.server.core.manager.SendSocketManager;
import com.game.server.core.redis.RedisManager;
import com.game.server.core.zero.Receive;
import com.game.server.core.zero.Send;
import com.game.server.eventGroup.GameEventHandler;
import com.game.server.eventGroup.RoomEventHandler;
import com.game.server.serverConfig.ServerConfig;

/**
 * Created by Administrator on 2020/7/3.
 */
public class GameServer {

    public void start() {

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

        //启动redis
        RedisManager.getInstance();

        // 开启1个线程去处理game
        new EventThreadGroup(MsgRegionConstant.MSG_REGION_GAME, GameEventHandler.class, 1);
        // 开启1个线程去处理room
        new EventThreadGroup(MsgRegionConstant.MSG_REGION_ROOM, RoomEventHandler.class, 1);
    }
}
