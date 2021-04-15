package com.game.server.query;

import bean.player.PlayerRole;
import com.game.server.redis.RedisCache;
import core.annotation.SqlAnnotation;
import org.redisson.api.RMapCache;

import static com.game.server.constant.SqlCmdConstant.PLAYER_ROLE_INSERT;
import static com.game.server.constant.SqlCmdConstant.PLAYER_ROLE_SELECT_ONE;
import static core.Constants.SQL_RESULT_SUCCESS;

public class PlayerRoleQuery {

    public static PlayerRole queryPlayerRole(int playerIndex) {
        RMapCache<Integer, PlayerRole> redisCache = RedisCache.getInstance().getRoleCache();
        PlayerRole playerRole = redisCache.get(playerIndex);
        if (playerRole == null) {
            playerRole = SqlAnnotation.getInstance().sqlSelectOne(PLAYER_ROLE_SELECT_ONE, playerIndex);
            if (playerRole != null) {
                redisCache.put(playerIndex, playerRole);
            }
        }
        return playerRole;
    }

    public static int createPlayerRole(int playerIndex, int job, int sex) {
        PlayerRole playerRole = new PlayerRole();
        playerRole.setPlayerIndex(playerIndex);
        playerRole.setJob(job);
        playerRole.setSex(sex);
        int result = SqlAnnotation.getInstance().executeCommitSql(PLAYER_ROLE_INSERT, playerRole);
        if (result == SQL_RESULT_SUCCESS) {
            RMapCache<Integer, PlayerRole> redisCache = RedisCache.getInstance().getRoleCache();
            redisCache.put(playerIndex, playerRole);
        }
        return result;
    }
}
