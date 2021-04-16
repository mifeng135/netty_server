package com.game.login.query;


import bean.login.LoginPlayerBean;
import com.game.login.redis.RedisCache;
import core.annotation.SqlAnnotation;
import org.redisson.api.RMap;

import static com.game.login.constant.SqlCmdConstant.PLAYER_INFO_INSERT;
import static com.game.login.constant.SqlCmdConstant.PLAYER_INFO_SELECT_ONE;
import static core.Constants.SQL_RESULT_SUCCESS;

public class PlayerInfoQuery {

    public static LoginPlayerBean queryPlayerInfo(String openId) {
        RMap<String, LoginPlayerBean> redisCache = RedisCache.getInstance().getPlayerInfoCache();
        LoginPlayerBean playerBean = redisCache.get(openId);
        if (playerBean == null) {
            playerBean = SqlAnnotation.getInstance().sqlSelectOne(PLAYER_INFO_SELECT_ONE, openId);
            if (playerBean != null) {
                redisCache.put(playerBean.getOpenId(), playerBean);
            }
        }
        return playerBean;
    }

    public static int createPlayer(String openId) {
        LoginPlayerBean playerBean = new LoginPlayerBean();
        playerBean.setServerInfo("");
        playerBean.setOpenId(openId);
        int result = SqlAnnotation.getInstance().executeCommitSql(PLAYER_INFO_INSERT, playerBean);
        if (result == SQL_RESULT_SUCCESS) {
            RMap<String, LoginPlayerBean> redisCache = RedisCache.getInstance().getPlayerInfoCache();
            redisCache.put(openId, playerBean);
        }
        return result;
    }
}
