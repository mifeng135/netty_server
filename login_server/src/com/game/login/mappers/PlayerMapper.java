package com.game.login.mappers;

import com.game.login.constant.SqlCmdConstant;
import com.game.login.core.annotation.SqlCmd;
import com.game.login.core.sql.SqlConstant;
import com.game.login.bean.PlayerBean;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Created by Administrator on 2020/6/5.
 */
public interface PlayerMapper {

    @SqlCmd(sqlCmd = 4444, sqlType = SqlConstant.SELECT_ONE)
    @Select("select * from t_player where player_index = #{playerIndex}")
    public PlayerBean findPlayerByIndex(@Param("playerIndex") int playerIndex);


    @SqlCmd(sqlCmd = 5555, sqlType = SqlConstant.SELECT_LIST)
    @Select("select * from t_player")
    public List<PlayerBean> findAll();

    @SqlCmd(sqlCmd = 6666, sqlType = SqlConstant.UPDATE)
    @Update("update t_player set platform_type = #{platform_type} where player_index = #{player_index}")
    public void updatePlayerPlatformTypeByIndex(PlayerBean playerBean);

    @SqlCmd(sqlCmd = SqlCmdConstant.PLAYER_SELECT_ACCOUNT_PASSWORD, sqlType = SqlConstant.SELECT_ONE)
    @Select("select id,name,account,password from game_player where account = #{account} and password = #{password}")
    public PlayerBean findPlayerWithAccountAndPassword(PlayerBean playerBean);
}
