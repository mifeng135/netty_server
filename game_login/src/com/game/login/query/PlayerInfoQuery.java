package com.game.login.query;


import bean.login.LoginPlayerInfoBean;
import com.game.login.redis.RedisCache;
import core.sql.SqlDao;
import core.util.TimeUtil;
import org.redisson.api.RMap;

import static core.Constants.SQL_MASTER;
import static core.Constants.SQL_RESULT_FAIL;
import static core.Constants.SQL_RESULT_SUCCESS;

public class PlayerInfoQuery {

    public static LoginPlayerInfoBean queryPlayerInfo(String openId) {
        RMap<String, LoginPlayerInfoBean> redisCache = RedisCache.getInstance().getPlayerInfoCache();
        LoginPlayerInfoBean playerBean = redisCache.get(openId);
        if (playerBean == null) {
            playerBean = SqlDao.getInstance().getDao(SQL_MASTER).fetch(LoginPlayerInfoBean.class, openId);
            if (playerBean != null) {
                redisCache.put(playerBean.getOpenId(), playerBean);
            }
        }
        return playerBean;
    }

    public static int createPlayer(String openId) {
        LoginPlayerInfoBean playerBean = new LoginPlayerInfoBean();
        playerBean.setOpenId(openId);
        playerBean.setLoginTime(TimeUtil.getCurrentTimeSecond());
        playerBean = SqlDao.getInstance().getDao(SQL_MASTER).insert(playerBean);
        if (playerBean != null) {
            RMap<String, LoginPlayerInfoBean> redisCache = RedisCache.getInstance().getPlayerInfoCache();
            redisCache.put(openId, playerBean);
            return SQL_RESULT_SUCCESS;
        }
        return SQL_RESULT_FAIL;
    }
}
