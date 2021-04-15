package com.game.server.query;

import bean.player.PlayerBean;
import com.game.server.redis.RedisCache;
import core.annotation.SqlAnnotation;
import org.redisson.api.RMapCache;

import static com.game.server.constant.SqlCmdConstant.PLAYER_INFO_INSERT;
import static com.game.server.constant.SqlCmdConstant.PLAYER_INFO_SELECT_ONE;
import static core.Constants.SQL_RESULT_SUCCESS;

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

    public static int createPlayer(String name, String loginIp, String openId) {
        PlayerBean playerBean = new PlayerBean();
        playerBean.setLoginIp(loginIp);
        playerBean.setName(name);
        playerBean.setOpenId(openId);
        int result = SqlAnnotation.getInstance().sqlSelectOne(PLAYER_INFO_INSERT, playerBean);
        if (result == SQL_RESULT_SUCCESS) {
            RMapCache<Integer, PlayerBean> redisCache = RedisCache.getInstance().getPlayerCache();
            redisCache.put(playerBean.getPlayerIndex(), playerBean);
        }
        return result;
    }
}
