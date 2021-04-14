package com.game.server.mappers;

import bean.login.LoginPlayerBean;
import com.game.server.constant.SqlCmdConstant;
import core.annotation.SqlCmd;
import core.sql.SqlConstant;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

public interface PlayerInfoMapper {

    @SqlCmd(sqlCmd = SqlCmdConstant.PLAYER_INFO_SELECT_ONE, sqlType = SqlConstant.SELECT_ONE)
    @Select("select player_index, open_id, server_info from game_player_info where open_id = #{openId} limit 1")
    LoginPlayerBean getPlayerInfo(LoginPlayerBean loginPlayerBean);

    @SqlCmd(sqlCmd = SqlCmdConstant.PLAYER_INFO_INSERT, sqlType = SqlConstant.INSERT)
    @Insert("insert into game_player_info (player_index, server_info, open_id)" + "values (#{playerIndex}, #{serverInfo}, #{openId})")
    @Options(useGeneratedKeys = true, keyProperty = "playerIndex")
    void insertPlayer(LoginPlayerBean loginPlayerBean);
}
