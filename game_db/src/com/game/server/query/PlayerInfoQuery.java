package com.game.server.query;

import com.game.server.bean.PlayerBean;
import com.game.server.redis.RedisCache;
import core.annotation.SqlAnnotation;
import org.redisson.api.RMapCache;

import static com.game.server.constant.SqlCmdConstant.PLAYER_INFO_SELECT_ONE;

public class PlayerInfoQuery {

    public static PlayerBean queryPlayer(int playerIndex) {
        RMapCache<Integer, PlayerBean> redisCache = RedisCache.getInstance().getPlayerCache();
        PlayerBean playerBean = redisCache.get(playerIndex);
        if (playerBean == null) {
            playerBean = SqlAnnotation.getInstance().sqlSelectOne(PLAYER_INFO_SELECT_ONE, playerIndex);
            if (playerBean != null) {
                redisCache.put(playerBean.getPlayerIndex(), playerBean);
            }
        }
        return playerBean;
    }

    public static void createPlayer(int playerIndex, String name) {

    }
}
