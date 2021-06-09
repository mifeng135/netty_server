package com.game.db.query;

import bean.player.PlayerSceneBean;
import com.game.db.redis.RedisCache;
import core.sql.SqlDao;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.redisson.api.RMapCache;

import static com.game.db.constant.GameConstant.MAP_INIT_ID;

public class PlayerSceneQuery {

    public static PlayerSceneBean queryScene(int playerIndex) {
        RMapCache<Integer, PlayerSceneBean> redisCache = RedisCache.getInstance().getSceneCache();
        PlayerSceneBean playerSceneBean = redisCache.get(playerIndex);
        if (playerSceneBean == null) {

        }
        return playerSceneBean;
    }

    public static boolean createScene(PlayerSceneBean playerScene) {
        playerScene = SqlDao.getInstance().getDao().insert(playerScene);
        if (playerScene != null) {

        }
        return false;
    }

    public static boolean createScene(int playerIndex) {
        PlayerSceneBean playerScene = new PlayerSceneBean();
        playerScene.setSceneId(MAP_INIT_ID);
        playerScene.setPlayerPositionX(400);
        playerScene.setPlayerPositionY(400);
        playerScene.setId(playerIndex);
        playerScene = SqlDao.getInstance().getDao().insert(playerScene);
        if (playerScene != null) {
            RMapCache<Integer, PlayerSceneBean> redisCache = RedisCache.getInstance().getSceneCache();
            redisCache.put(playerIndex, playerScene);
            return true;
        }
        return false;
    }

    public static boolean updateScene(int playerIndex, int sceneId, int positionX, int positionY) {
        boolean result = SqlDao.getInstance().getDao().update(PlayerSceneBean.class,
                Chain.make("player_position_x", positionX).
                        add("player_position_y", positionY).
                        add("scene_id", sceneId),
                Cnd.where("player_index", "=", playerIndex)) > 0;
        if (result) {
            PlayerSceneBean playerScene = new PlayerSceneBean();
            playerScene.setId(playerIndex);
            playerScene.setPlayerPositionX(positionX);
            playerScene.setPlayerPositionY(positionY);
            playerScene.setSceneId(sceneId);
            RMapCache<Integer, PlayerSceneBean> redisCache = RedisCache.getInstance().getSceneCache();
            redisCache.put(playerIndex, playerScene);
        }
        return result;
    }
}
