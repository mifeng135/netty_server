package com.game.server.mappers;

import com.game.server.constant.SqlCmdConstant;
import com.game.server.core.annotation.SqlCmd;
import com.game.server.core.sql.SqlConstant;
import com.game.server.bean.PlayerBean;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Created by Administrator on 2020/6/5.
 */
public interface PlayerMapper {

    @SqlCmd(sqlCmd = 5555, sqlType = SqlConstant.SELECT_LIST)
    @Select("select * from t_player")
    public List<PlayerBean> findAll();

    @SqlCmd(sqlCmd = SqlCmdConstant.PLAYER_SELECT_USER_INFO, sqlType = SqlConstant.SELECT_ONE)
    @Update("select name from game_player where id = #{id}")
    public PlayerBean getPlayerInfoById(@Param("id") int id);
}