package com.game.db.query;

import bean.player.PlayerBean;
import com.game.db.PropertiesConfig;
import com.game.db.redis.RedisCache;
import core.sql.SqlDao;
import org.nutz.dao.Cnd;
import org.redisson.api.RMapCache;


public class PlayerInfoQuery {

    public static PlayerBean queryPlayer(int playerIndex) {
        RMapCache<Integer, PlayerBean> redisCache = RedisCache.getInstance().getPlayerCache();
        PlayerBean playerBean = redisCache.get(playerIndex);
        if (playerBean == null) {
            playerBean = SqlDao.getInstance().getDao().fetch(PlayerBean.class,
                    Cnd.where("player_index", "=", playerIndex));
            if (playerBean != null) {
                redisCache.put(playerBean.getPlayerIndex(), playerBean);
            }
        }
        return playerBean;
    }


    public static boolean createPlayer(PlayerBean playerBean) {
        playerBean = SqlDao.getInstance().getDao().insert(playerBean);
        if (playerBean != null) {
            RMapCache<Integer, PlayerBean> redisCache = RedisCache.getInstance().getPlayerCache();
            redisCache.put(playerBean.getPlayerIndex(), playerBean);
            return true;
        }
        return false;
    }


    public static boolean createPlayer(int playerIndex, String name, String loginIp, String openId) {
        PlayerBean playerBean = new PlayerBean();
        playerBean.setLoginIp(loginIp);
        playerBean.setName(name);
        playerBean.setOpenId(openId);
        playerBean.setPlayerIndex(playerIndex);
        playerBean.setServerId(PropertiesConfig.serverId);
        playerBean = SqlDao.getInstance().getDao().insert(playerBean);
        if (playerBean != null) {
            RMapCache<Integer, PlayerBean> redisCache = RedisCache.getInstance().getPlayerCache();
            redisCache.put(playerBean.getPlayerIndex(), playerBean);
            return true;
        }
        return false;
    }
}
