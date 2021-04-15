package com.game.server.query;

import bean.player.PlayerScene;
import com.game.server.redis.RedisCache;
import core.annotation.SqlAnnotation;
import org.redisson.api.RMapCache;

import static com.game.server.constant.GameConstant.MAP_INIT_ID;
import static com.game.server.constant.SqlCmdConstant.PLAYER_SCENE_INSERT_SCENE_INFO;
import static com.game.server.constant.SqlCmdConstant.PLAYER_SCENE_SELECT_SCENE_INFO;
import static com.game.server.constant.SqlCmdConstant.PLAYER_SCENE_UPDATE_SCENE_INFO;
import static core.Constants.SQL_RESULT_SUCCESS;

public class PlayerSceneQuery {

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

    public static int createScene(int playerIndex) {
        PlayerScene playerScene = new PlayerScene();
        playerScene.setId(MAP_INIT_ID);
        playerScene.setPlayerPositionX(400.00f);
        playerScene.setPlayerPositionY(400.00f);
        playerScene.setPlayerIndex(playerIndex);
        int result = SqlAnnotation.getInstance().sqlSelectOne(PLAYER_SCENE_INSERT_SCENE_INFO, playerScene);
        if (result == SQL_RESULT_SUCCESS) {
            RMapCache<Integer, PlayerScene> redisCache = RedisCache.getInstance().getSceneCache();
            redisCache.put(playerIndex, playerScene);
        }
        return result;
    }

    public static int updateScene(int playerIndex, int sceneId, float positionX, float positionY) {
        PlayerScene playerScene = new PlayerScene();
        playerScene.setPlayerIndex(playerIndex);
        playerScene.setPlayerPositionX(positionX);
        playerScene.setPlayerPositionY(positionY);
        playerScene.setSceneId(sceneId);
        int result = SqlAnnotation.getInstance().sqlSelectOne(PLAYER_SCENE_UPDATE_SCENE_INFO, playerScene);
        if (result == SQL_RESULT_SUCCESS) {
            RMapCache<Integer, PlayerScene> redisCache = RedisCache.getInstance().getSceneCache();
            redisCache.put(playerIndex, playerScene);
        }
        return result;
    }
}
