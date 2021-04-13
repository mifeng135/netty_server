package com.game.server.mappers;

import com.game.server.bean.PlayerBean;
import com.game.server.constant.SqlCmdConstant;
import core.annotation.SqlCmd;
import core.sql.SqlConstant;
import org.apache.ibatis.annotations.Select;


/**
 * Created by Administrator on 2020/6/5.
 */
public interface PlayerMapper {

    @SqlCmd(sqlCmd = SqlCmdConstant.PLAYER_INFO_SELECT_ONE, sqlType = SqlConstant.SELECT_ONE)
    @Select("select player_index, name from game_player where player_index = #{playerIndex} limit 1")
    PlayerBean getPlayerInfoByIndex(PlayerBean playerBean);
}
