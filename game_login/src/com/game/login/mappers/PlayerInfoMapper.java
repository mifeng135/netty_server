package com.game.login.mappers;

import bean.login.PlayerLoginBean;
import com.game.login.constant.SqlCmdConstant;
import core.annotation.SqlCmd;
import core.sql.SqlConstant;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface PlayerInfoMapper {

    @SqlCmd(sqlCmd = SqlCmdConstant.PLAYER_INFO_SELECT_ONE, sqlType = SqlConstant.SELECT_ONE)
    @Select("select player_index, open_id, login_time from game_player_info where open_id = #{openId} limit 1")
    PlayerLoginBean getPlayerInfo(@Param("openId") String openId);

    @SqlCmd(sqlCmd = SqlCmdConstant.PLAYER_INFO_INSERT, sqlType = SqlConstant.INSERT)
    @Insert("insert into game_player_info (open_id, login_time)" + "values (#{openId}, #{loginTime})")
    @Options(useGeneratedKeys = true, keyProperty = "playerIndex")
    void insertPlayer(PlayerLoginBean loginPlayerBean);

    @SqlCmd(sqlCmd = SqlCmdConstant.PLAYER_INFO_SELECT_LAST_LOGIN, sqlType = SqlConstant.SELECT_LIST)
    @Select("select open_id, player_index, login_time from game_player_info order by login_time desc limit 10000")
    List<PlayerLoginBean> getLastLogin();
}