package com.game.db.query;

import bean.player.PlayerInfoBean;
import com.game.db.PropertiesConfig;
import com.game.db.redis.RedisCache;
import core.sql.SqlDao;
import org.nutz.dao.Cnd;
import org.redisson.api.RMapCache;


public class PlayerInfoQuery {

    public static PlayerInfoBean queryPlayer(int playerIndex) {
        RMapCache<Integer, PlayerInfoBean> redisCache = RedisCache.getInstance().getPlayerCache();
        PlayerInfoBean playerBean = redisCache.get(playerIndex);
        if (playerBean == null) {
            playerBean = SqlDao.getInstance().getDao().fetch(PlayerInfoBean.class,
                    Cnd.where("player_index", "=", playerIndex));
        }
        return playerBean;
    }


    public static boolean createPlayer(PlayerInfoBean playerBean) {
        playerBean = SqlDao.getInstance().getDao().insert(playerBean);
        return false;
    }


    public static boolean createPlayer(int playerIndex, String name, String loginIp, String openId) {
        PlayerInfoBean playerBean = new PlayerInfoBean();
        playerBean.setLoginIp(loginIp);
        playerBean.setName(name);
        playerBean.setOpenId(openId);
        playerBean.setId(playerIndex);
        playerBean.setServerId(PropertiesConfig.serverId);
        playerBean = SqlDao.getInstance().getDao().insert(playerBean);
        return false;
    }
}
