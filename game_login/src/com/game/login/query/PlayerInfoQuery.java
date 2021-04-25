package com.game.login.query;


import bean.login.LoginPlayerInfoBean;
import com.game.login.redis.RedisCache;
import core.sql.SqlDao;
import core.util.TimeUtil;
import org.redisson.api.RMap;


public class PlayerInfoQuery {

    public static LoginPlayerInfoBean queryPlayerInfo(String openId) {
        RMap<String, LoginPlayerInfoBean> redisCache = RedisCache.getInstance().getPlayerInfoCache();
        LoginPlayerInfoBean playerBean = redisCache.get(openId);
        if (playerBean == null) {
            playerBean = SqlDao.getInstance().getDao().fetch(LoginPlayerInfoBean.class, openId);
            if (playerBean != null) {
                redisCache.put(playerBean.getOpenId(), playerBean);
            }
        }
        return playerBean;
    }

    public static LoginPlayerInfoBean createPlayer(String openId) {
        LoginPlayerInfoBean playerBean = new LoginPlayerInfoBean();
        playerBean.setOpenId(openId);
        playerBean.setLoginTime(TimeUtil.getCurrentTimeSecond());
        playerBean = SqlDao.getInstance().getDao().insert(playerBean);
        if (playerBean != null) {
            RMap<String, LoginPlayerInfoBean> redisCache = RedisCache.getInstance().getPlayerInfoCache();
            redisCache.put(openId, playerBean);
            return playerBean;
        }
        return null;
    }
}
