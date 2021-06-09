package com.game.db.query;

import bean.player.PlayerRoleBean;
import com.game.db.redis.RedisCache;
import core.sql.SqlDao;
import org.nutz.dao.Cnd;
import org.redisson.api.RMapCache;


public class PlayerRoleQuery {

    public static PlayerRoleBean queryPlayerRole(int playerIndex) {
        RMapCache<Integer, PlayerRoleBean> redisCache = RedisCache.getInstance().getRoleCache();
        PlayerRoleBean playerRole = redisCache.get(playerIndex);
        if (playerRole == null) {
            playerRole = SqlDao.getInstance().getDao().fetch(PlayerRoleBean.class,
                    Cnd.where("player_index", "=", playerIndex));
            if (playerRole != null) {
                redisCache.put(playerIndex, playerRole);
            }
        }
        return playerRole;
    }

    public static boolean createPlayerRole(PlayerRoleBean playerRole) {
        playerRole = SqlDao.getInstance().getDao().insert(playerRole);
        return false;
    }

    public static boolean createPlayerRole(int playerIndex, int job, int sex) {
        PlayerRoleBean playerRole = new PlayerRoleBean();
        return false;
    }
}
