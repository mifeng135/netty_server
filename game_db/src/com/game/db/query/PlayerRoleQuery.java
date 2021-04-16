package com.game.db.query;

import bean.player.PlayerRoleBean;
import com.game.db.redis.RedisCache;
import core.annotation.SqlAnnotation;
import org.redisson.api.RMapCache;

import static com.game.db.constant.SqlCmdConstant.PLAYER_ROLE_INSERT;
import static com.game.db.constant.SqlCmdConstant.PLAYER_ROLE_SELECT_ONE;
import static core.Constants.SQL_RESULT_SUCCESS;

public class PlayerRoleQuery {

    public static PlayerRoleBean queryPlayerRole(int playerIndex) {
        RMapCache<Integer, PlayerRoleBean> redisCache = RedisCache.getInstance().getRoleCache();
        PlayerRoleBean playerRole = redisCache.get(playerIndex);
        if (playerRole == null) {
            playerRole = SqlAnnotation.getInstance().sqlSelectOne(PLAYER_ROLE_SELECT_ONE, playerIndex);
            if (playerRole != null) {
                redisCache.put(playerIndex, playerRole);
            }
        }
        return playerRole;
    }

    public static int createPlayerRole(PlayerRoleBean playerRole) {
        int result = SqlAnnotation.getInstance().executeCommitSql(PLAYER_ROLE_INSERT, playerRole);
        if (result == SQL_RESULT_SUCCESS) {
            RMapCache<Integer, PlayerRoleBean> redisCache = RedisCache.getInstance().getRoleCache();
            redisCache.put(playerRole.getPlayerIndex(), playerRole);
        }
        return result;
    }

    public static int createPlayerRole(int playerIndex, int job, int sex) {
        PlayerRoleBean playerRole = new PlayerRoleBean();
        playerRole.setPlayerIndex(playerIndex);
        playerRole.setJob(job);
        playerRole.setSex(sex);
        int result = SqlAnnotation.getInstance().executeCommitSql(PLAYER_ROLE_INSERT, playerRole);
        if (result == SQL_RESULT_SUCCESS) {
            RMapCache<Integer, PlayerRoleBean> redisCache = RedisCache.getInstance().getRoleCache();
            redisCache.put(playerIndex, playerRole);
        }
        return result;
    }
}
