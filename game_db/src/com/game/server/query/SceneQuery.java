package com.game.server.query;

import bean.player.PlayerScene;
import com.game.server.redis.RedisCache;
import core.annotation.SqlAnnotation;
import org.redisson.api.RMapCache;

import static com.game.server.constant.SqlCmdConstant.PLAYER_SCENE_SELECT_SCENE_INFO;

public class SceneQuery {

    public static PlayerScene queryScene(int playerIndex) {
        RMapCache<Integer, PlayerScene> redisCache = RedisCache.getInstance().getSceneCache();
        PlayerScene playerScene = redisCache.get(playerIndex);
        if (playerScene == null) {
            playerScene = SqlAnnotation.getInstance().sqlSelectOne(PLAYER_SCENE_SELECT_SCENE_INFO, playerIndex);
            if (playerScene != null) {
                redisCache.put(playerScene.getPlayerIndex(), playerScene);
            }
        }
        return playerScene;
    }
}
