package com.game.db.mappers;

import bean.player.PlayerBean;
import com.game.db.constant.SqlCmdConstant;
import core.annotation.SqlCmd;
import core.sql.SqlConstant;
import org.apache.ibatis.annotations.*;


/**
 * Created by Administrator on 2020/6/5.
 */
public interface PlayerMapper {

    @SqlCmd(sqlCmd = SqlCmdConstant.PLAYER_INFO_SELECT_ONE, sqlType = SqlConstant.SELECT_ONE)
    @Select("select player_index, name, server_id from game_player where player_index = #{playerIndex} limit 1")
    PlayerBean getPlayerInfoByIndex(@Param("playerIndex") int playerIndex);


    @SqlCmd(sqlCmd = SqlCmdConstant.PLAYER_INFO_UPDATE_LOGIN, sqlType = SqlConstant.UPDATE)
    @Update("update game_player set login_ip = #{loginIp},last_login_time = #{lastLoginTime} where player_index = #{playerIndex}")
    void updatePlayerInfo(PlayerBean playerBean);

    @SqlCmd(sqlCmd = SqlCmdConstant.PLAYER_INFO_UPDATE_HEADER, sqlType = SqlConstant.UPDATE)
    @Update("update game_player set header = #{header} where player_index = #{playerIndex}")
    void updatePlayerHeader(PlayerBean playerBean);


    @SqlCmd(sqlCmd = SqlCmdConstant.PLAYER_INFO_DELETE, sqlType = SqlConstant.DELETE)
    @Delete("delete from game_player where player_index = #{playerIndex}")
    void deletePlayer(@Param("playerIndex") int playerIndex);


    @SqlCmd(sqlCmd = SqlCmdConstant.PLAYER_INFO_INSERT, sqlType = SqlConstant.INSERT)
    @Insert("INSERT INTO game_player (player_index, register_time, login_ip, server_id, open_id)" +
            "VALUES (#{playerIndex}, #{registerTime}, #{loginIp}, #{serverId}, #{openId})")
    void insertPlayerInfo(PlayerBean playerBean);

}
