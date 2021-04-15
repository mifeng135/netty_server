package com.game.server.mappers;

import bean.player.PlayerSceneBean;
import com.game.server.constant.SqlCmdConstant;
import core.annotation.SqlCmd;
import core.sql.SqlConstant;
import org.apache.ibatis.annotations.*;

public interface PlayerSceneMapper {

    @SqlCmd(sqlCmd = SqlCmdConstant.PLAYER_SCENE_SELECT_SCENE_INFO, sqlType = SqlConstant.SELECT_ONE)
    @Select("select * from game_player_scene where player_index = #{playerIndex} limit 1")
    PlayerSceneBean getSceneInfoByPlayerIndex(@Param("playerIndex") int playerIndex);

    @SqlCmd(sqlCmd = SqlCmdConstant.PLAYER_SCENE_UPDATE_SCENE_INFO, sqlType = SqlConstant.UPDATE)
    @Update("update game_player_scene set " +
            "player_position_x = #{playerPositionX}," +
            "player_position_y = #{playerPositionY}," +
            "scene_id = #{sceneId} " +
            "where player_index = #{playerIndex}")
    void updateSceneByPlayerIndex(PlayerSceneBean playerScene);


    @SqlCmd(sqlCmd = SqlCmdConstant.PLAYER_SCENE_INSERT_SCENE_INFO, sqlType = SqlConstant.INSERT)
    @Insert("INSERT INTO game_player_scene (player_index, player_position_x, player_position_y, scene_id) " +
            "VALUES (#{playerIndex}, #{playerPositionX}, #{playerPositionY}, #{sceneId})")
    void insertPlayerSceneInfo(PlayerSceneBean playerBean);


    @SqlCmd(sqlCmd = SqlCmdConstant.PLAYER_SCENE_DELETE_SCENE_INFO, sqlType = SqlConstant.DELETE)
    @Insert("delete from game_player_scene where player_index = #{playerIndex}")
    void deletePlayerSceneInfo(@Param("playerIndex") int playerIndex);
}
