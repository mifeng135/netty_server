package com.game.server.server;

import com.game.server.core.redis.RedisManager;
import com.game.server.eventGroup.game.GameEventGroup;
import com.game.server.eventGroup.game.GameEventHandler;
import com.game.server.eventGroup.room.RoomEventGroup;
import com.game.server.eventGroup.room.RoomEventHandler;
import com.game.server.room.Player;
import com.game.server.socket.ReceiveFromGate;
import com.game.server.socket.SendToGate;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2020/7/3.
 */
public class GameServer {

    public void start() {

        //启动redis
        RedisManager.getInstance();

        //启动发送gate的socket
        SendToGate.getInstance().start();

        //启动接受gate的socket
        new ReceiveFromGate().start();

        // 开辟1个线程去处理game
        new GameEventGroup(GameEventHandler.class, 1);

        // 开辟1个线程去处理room
        new RoomEventGroup(RoomEventHandler.class, 1);
    }
}
