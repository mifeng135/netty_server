package com.game.db.mappers;

import bean.player.PlayerRoleBean;
import com.game.db.constant.SqlCmdConstant;
import core.annotation.SqlCmd;
import core.sql.SqlConstant;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface PlayerRoleMapper {

    @SqlCmd(sqlCmd = SqlCmdConstant.PLAYER_ROLE_SELECT_ONE, sqlType = SqlConstant.SELECT_LIST)
    @Select("select job, sex from game_player_role where player_index = #{playerIndex}")
    List<PlayerRoleBean> getPlayerRole(@Param("playerIndex") int playerIndex);

    @SqlCmd(sqlCmd = SqlCmdConstant.PLAYER_ROLE_INSERT, sqlType = SqlConstant.INSERT)
    @Insert("insert into game_player_role (player_index, job, sex)" +
            "values (#{playerIndex}, #{job}, #{sex})")
    List<PlayerRoleBean> inserRole(PlayerRoleBean playerItem);
}
