package com.game.login.query;


import bean.login.PlayerLoginBean;
import com.game.login.redis.RedisCache;
import core.annotation.SqlAnnotation;
import core.sql.SqlDao;
import core.util.TimeUtil;
import org.redisson.api.RMap;

import static core.Constants.SQL_MASTER;
import static core.Constants.SQL_RESULT_FAIL;
import static core.Constants.SQL_RESULT_SUCCESS;

public class PlayerInfoQuery {

    public static PlayerLoginBean queryPlayerInfo(String openId) {
        RMap<String, PlayerLoginBean> redisCache = RedisCache.getInstance().getPlayerInfoCache();
        PlayerLoginBean playerBean = redisCache.get(openId);
        if (playerBean == null) {
            playerBean = SqlDao.getInstance().getDao(SQL_MASTER).fetch(PlayerLoginBean.class, openId);
            if (playerBean != null) {
                redisCache.put(playerBean.getOpenId(), playerBean);
            }
        }
        return playerBean;
    }

    public static int createPlayer(String openId) {
        PlayerLoginBean playerBean = new PlayerLoginBean();
        playerBean.setOpenId(openId);
        playerBean.setLoginTime(TimeUtil.getCurrentTimeSecond());
        playerBean = SqlDao.getInstance().getDao(SQL_MASTER).insert(playerBean);
        if (playerBean != null) {
            RMap<String, PlayerLoginBean> redisCache = RedisCache.getInstance().getPlayerInfoCache();
            redisCache.put(openId, playerBean);
            return SQL_RESULT_SUCCESS;
        }
        return SQL_RESULT_FAIL;
    }
}
