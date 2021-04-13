package com.game.server.mappers;

import com.game.server.bean.PlayerBean;
import com.game.server.constant.SqlCmdConstant;
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
    void deletePlayer(PlayerBean playerBean);


    @SqlCmd(sqlCmd = SqlCmdConstant.PLAYER_INFO_INSERT, sqlType = SqlConstant.INSERT)
    @Insert("INSERT INTO game_player (account, password, register_time, login_ip, last_login_time) " +
            "VALUES (#{account}, #{password}, #{registerTime}, #{loginIp}, #{lastLoginTime})")
    void insertPlayerInfo(PlayerBean playerBean);

}
