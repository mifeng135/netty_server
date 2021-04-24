package com.game.db.query;

import bean.player.PlayerRoleBean;
import com.game.db.redis.RedisCache;
import core.sql.SqlDao;
import org.nutz.dao.Cnd;
import org.redisson.api.RMapCache;

import static core.Constants.SQL_MASTER;

public class PlayerRoleQuery {

    public static PlayerRoleBean queryPlayerRole(int playerIndex) {
        RMapCache<Integer, PlayerRoleBean> redisCache = RedisCache.getInstance().getRoleCache();
        PlayerRoleBean playerRole = redisCache.get(playerIndex);
        if (playerRole == null) {
            playerRole = SqlDao.getInstance().getDao(SQL_MASTER).fetch(PlayerRoleBean.class,
                    Cnd.where("player_index", "=", playerIndex));
            if (playerRole != null) {
                redisCache.put(playerIndex, playerRole);
            }
        }
        return playerRole;
    }

    public static boolean createPlayerRole(PlayerRoleBean playerRole) {
        playerRole = SqlDao.getInstance().getDao(SQL_MASTER).insert(playerRole);
        if (playerRole != null) {
            RMapCache<Integer, PlayerRoleBean> redisCache = RedisCache.getInstance().getRoleCache();
            redisCache.put(playerRole.getPlayerIndex(), playerRole);
            return true;
        }
        return false;
    }

    public static boolean createPlayerRole(int playerIndex, int job, int sex) {
        PlayerRoleBean playerRole = new PlayerRoleBean();
        playerRole.setPlayerIndex(playerIndex);
        playerRole.setJob(job);
        playerRole.setSex(sex);
        playerRole = SqlDao.getInstance().getDao(SQL_MASTER).insert(playerRole);
        if (playerRole != null) {
            RMapCache<Integer, PlayerRoleBean> redisCache = RedisCache.getInstance().getRoleCache();
            redisCache.put(playerIndex, playerRole);
            return true;
        }
        return false;
    }
}
