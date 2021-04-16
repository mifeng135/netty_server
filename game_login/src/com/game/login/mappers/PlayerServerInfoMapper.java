package com.game.login.mappers;

import bean.player.PlayerServerInfoBean;
import com.game.login.constant.SqlCmdConstant;
import core.annotation.SqlCmd;
import core.sql.SqlConstant;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface PlayerServerInfoMapper {

    @SqlCmd(sqlCmd = SqlCmdConstant.PLAYER_INFO_SELECT_ONE, sqlType = SqlConstant.SELECT_ONE)
    @Select("select player_index, server_id from game_player_server_info where player_index = #{playerIndex}")
    List<PlayerServerInfoBean> getPlayerServerInfo(@Param("playerIndex") int playerIndex);


    @SqlCmd(sqlCmd = SqlCmdConstant.PLAYER_SERVER_INFO_SELECT_ALL, sqlType = SqlConstant.SELECT_MAP)
    @Select("select m.player_index, server_id from game_player_info as t "
    + "left join game_player_server_info as m "
    + "on t.player_index = m.player_index")
    Map<Integer,List<PlayerServerInfoBean>> getPlayerServer();
}
