package com.game.server.query;

import com.game.server.bean.PlayerScene;
import com.game.server.redis.RedisCache;
import core.annotation.SqlAnnotation;
import org.redisson.api.RMapCache;

import static com.game.server.constant.SqlCmdConstant.PLAYER_SCENE_SELECT_SCENE_INFO;

public class QueryPlayerInfo {

    public static PlayerScene queryScene(int playerIndex) {
        RMapCache<Integer, PlayerScene> redisCache = RedisCache.getInstance().getSceneCache();
        PlayerScene playerScene = redisCache.get(playerIndex);
        if (playerScene == null) {
            playerScene = SqlAnnotation.getInstance().sqlSelectOne(PLAYER_SCENE_SELECT_SCENE_INFO, playerIndex);
        }
        return playerScene;
    }
}
