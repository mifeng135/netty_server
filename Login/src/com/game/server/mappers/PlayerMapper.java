package com.game.server.mappers;

import com.game.server.constant.SqlCmdConstant;
import com.game.server.bean.PlayerBean;
import core.annotation.SqlCmd;
import core.sql.SqlConstant;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;


/**
 * Created by Administrator on 2020/6/5.
 */
public interface PlayerMapper {

    @SqlCmd(sqlCmd = SqlCmdConstant.PLAYER_SELECT_ACCOUNT_PASSWORD, sqlType = SqlConstant.SELECT_ONE)
    @Select("select * from game_player where account = #{account} and password = #{password}")
    public PlayerBean findPlayerWithAccountAndPassword(PlayerBean playerBean);


    @SqlCmd(sqlCmd = SqlCmdConstant.PLAYER_UPDATE_LOGIN_INFO, sqlType = SqlConstant.UPDATE)
    @Update("update game_player set loginIp = #{loginIp},lastLoginTime = #{lastLoginTime} where id = #{id}")
    public void updatePlayerInfo(PlayerBean playerBean);

}
