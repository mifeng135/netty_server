package com.game.login.query;


import bean.login.LoginPlayerInfoBean;
import core.util.Instance;
import core.util.TimeUtil;

import static constants.RedisConstant.REDIS_PLAYER_OPEN_INFO_LIST;


public class PlayerInfoQuery {

    public static LoginPlayerInfoBean queryPlayerInfo(String openId) {
        LoginPlayerInfoBean playerBean = Instance.redis().cacheMapGet(REDIS_PLAYER_OPEN_INFO_LIST, openId);
        if (playerBean == null) {
            playerBean = Instance.sql().fetch(LoginPlayerInfoBean.class, openId);
            if (playerBean != null) {
                Instance.redis().cacheMapPut(REDIS_PLAYER_OPEN_INFO_LIST, playerBean.getOpenId(), playerBean);
            }
        }
        return playerBean;
    }

    public static LoginPlayerInfoBean createPlayer(String openId) {
        LoginPlayerInfoBean playerBean = new LoginPlayerInfoBean();
        playerBean.setOpenId(openId);
        playerBean.setLoginTime(TimeUtil.getCurrentTimeSecond());
        playerBean = Instance.sql().insert(playerBean);
        if (playerBean != null) {
            Instance.redis().cacheMapPut(REDIS_PLAYER_OPEN_INFO_LIST, openId, playerBean);
            return playerBean;
        }
        return null;
    }
}
