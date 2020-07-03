package com.game.server.server;

import com.game.server.core.redis.RedisManager;
import com.game.server.eventGroup.game.GameEventGroup;
import com.game.server.eventGroup.game.GameEventHandler;
import com.game.server.eventGroup.room.RoomEventGroup;
import com.game.server.eventGroup.room.RoomEventHandler;
import com.game.server.socket.ReceiveFromGate;
import com.game.server.socket.SendToGate;

/**
 * Created by Administrator on 2020/7/3.
 */
public class GameServer {

    public void start() {
        RedisManager.getInstance();
        SendToGate.getInstance().start();
        new ReceiveFromGate().start();

        new GameEventGroup(GameEventHandler.class, 1);
        new RoomEventGroup(RoomEventHandler.class, 1);
    }
}
