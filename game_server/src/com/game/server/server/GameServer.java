package com.game.server.server;

import com.game.server.core.redis.RedisManager;

/**
 * Created by Administrator on 2020/7/3.
 */
public class GameServer {

    public void start() {
        RedisManager.getInstance();
    }
}
