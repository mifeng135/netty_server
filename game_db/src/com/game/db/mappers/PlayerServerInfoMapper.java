package com.game.db.mappers;

import bean.login.LoginPlayerServerInfoBean;
import com.game.db.constant.SqlCmdConstant;
import core.annotation.SqlCmd;
import core.sql.SqlConstant;
import org.apache.ibatis.annotations.Insert;

import java.util.List;

public interface PlayerServerInfoMapper {

    @SqlCmd(sqlCmd = SqlCmdConstant.PLAYER_SERVER_INFO_INSERT, sqlType = SqlConstant.INSERT)
    @Insert("insert into game_player_server_info (player_index, server_id)" + "values (#{playerIndex}, #{serverId})")
    List<LoginPlayerServerInfoBean> inserRole(LoginPlayerServerInfoBean playerItem);
}
