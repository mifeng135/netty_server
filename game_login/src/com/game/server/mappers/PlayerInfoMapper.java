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
    @Select("select player_index, server_info from player_info where player_index = #{playerIndex} limit 1")
    LoginPlayerBean getPlayerInfo(LoginPlayerBean loginPlayerBean);

    @SqlCmd(sqlCmd = SqlCmdConstant.PLAYER_INFO_INSERT, sqlType = SqlConstant.INSERT)
    @Insert("insert into player_info (player_index, server_info)" + "values (#{playerIndex}, #{serverInfo})")
    @Options(useGeneratedKeys = true, keyProperty = "playerIndex")
    void insertPlayer(LoginPlayerBean loginPlayerBean);
}
