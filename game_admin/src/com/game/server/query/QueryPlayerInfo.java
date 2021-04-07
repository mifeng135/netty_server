package com.game.server.query;

import com.game.server.bean.PlayerBean;
import com.game.server.bean.PlayerScene;
import com.game.server.constant.SqlCmdConstant;
import com.game.server.redis.RedisCache;
import core.annotation.SqlAnnotation;
import org.redisson.api.RMapCache;

import static com.game.server.constant.SqlCmdConstant.PLAYER_SCENE_SELECT_SCENE_INFO;

public class QueryPlayerInfo {

    public static PlayerBean queryPlayerInfo(String account, String password) {
        RMapCache<String, PlayerBean> redisCache = RedisCache.getInstance().getAccountLoginCache();
        PlayerBean playerBean = redisCache.get(account);
        if (playerBean != null && !playerBean.getPassword().equals(password)) {
            return null;
        }
        playerBean = new PlayerBean();
        playerBean.setAccount(account);
        playerBean.setPassword(password);
        return SqlAnnotation.getInstance().sqlSelectOne(SqlCmdConstant.PLAYER_SELECT_ACCOUNT_PASSWORD, playerBean);
    }

    public static PlayerScene queryScene(int playerIndex) {
        RMapCache<Integer, PlayerScene> redisCache = RedisCache.getInstance().getSceneCache();
        PlayerScene playerScene = redisCache.get(playerIndex);
        if (playerScene == null) {
            playerScene = SqlAnnotation.getInstance().sqlSelectOne(PLAYER_SCENE_SELECT_SCENE_INFO, playerIndex);
        }
        return playerScene;
    }
}
