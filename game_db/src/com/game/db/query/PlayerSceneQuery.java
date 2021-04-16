package com.game.db.query;

import bean.player.PlayerSceneBean;
import com.game.db.redis.RedisCache;
import core.annotation.SqlAnnotation;
import org.redisson.api.RMapCache;

import static com.game.db.constant.GameConstant.MAP_INIT_ID;
import static com.game.db.constant.SqlCmdConstant.PLAYER_SCENE_INSERT_SCENE_INFO;
import static com.game.db.constant.SqlCmdConstant.PLAYER_SCENE_SELECT_SCENE_INFO;
import static com.game.db.constant.SqlCmdConstant.PLAYER_SCENE_UPDATE_SCENE_INFO;
import static core.Constants.SQL_RESULT_SUCCESS;

public class PlayerSceneQuery {

    public static PlayerSceneBean queryScene(int playerIndex) {
        RMapCache<Integer, PlayerSceneBean> redisCache = RedisCache.getInstance().getSceneCache();
        PlayerSceneBean playerSceneBean = redisCache.get(playerIndex);
        if (playerSceneBean == null) {
            playerSceneBean = SqlAnnotation.getInstance().sqlSelectOne(PLAYER_SCENE_SELECT_SCENE_INFO, playerIndex);
            if (playerSceneBean != null) {
                redisCache.put(playerSceneBean.getPlayerIndex(), playerSceneBean);
            }
        }
        return playerSceneBean;
    }

    public static int createScene(PlayerSceneBean playerScene) {
        int result = SqlAnnotation.getInstance().sqlSelectOne(PLAYER_SCENE_INSERT_SCENE_INFO, playerScene);
        if (result == SQL_RESULT_SUCCESS) {
            RMapCache<Integer, PlayerSceneBean> redisCache = RedisCache.getInstance().getSceneCache();
            redisCache.put(playerScene.getPlayerIndex(), playerScene);
        }
        return result;
    }

    public static int createScene(int playerIndex) {
        PlayerSceneBean playerScene = new PlayerSceneBean();
        playerScene.setSceneId(MAP_INIT_ID);
        playerScene.setPlayerPositionX(400);
        playerScene.setPlayerPositionY(400);
        playerScene.setPlayerIndex(playerIndex);
        int result = SqlAnnotation.getInstance().sqlSelectOne(PLAYER_SCENE_INSERT_SCENE_INFO, playerScene);
        if (result == SQL_RESULT_SUCCESS) {
            RMapCache<Integer, PlayerSceneBean> redisCache = RedisCache.getInstance().getSceneCache();
            redisCache.put(playerIndex, playerScene);
        }
        return result;
    }

    public static int updateScene(int playerIndex, int sceneId, int positionX, int positionY) {
        PlayerSceneBean playerScene = new PlayerSceneBean();
        playerScene.setPlayerIndex(playerIndex);
        playerScene.setPlayerPositionX(positionX);
        playerScene.setPlayerPositionY(positionY);
        playerScene.setSceneId(sceneId);
        int result = SqlAnnotation.getInstance().sqlSelectOne(PLAYER_SCENE_UPDATE_SCENE_INFO, playerScene);
        if (result == SQL_RESULT_SUCCESS) {
            RMapCache<Integer, PlayerSceneBean> redisCache = RedisCache.getInstance().getSceneCache();
            redisCache.put(playerIndex, playerScene);
        }
        return result;
    }
}
