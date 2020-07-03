package com.game.game.server;

import com.game.game.core.redis.RedisManager;

/**
 * Created by Administrator on 2020/7/3.
 */
public class GameServer {

    public void start() {
        RedisManager.getInstance();
    }
}
