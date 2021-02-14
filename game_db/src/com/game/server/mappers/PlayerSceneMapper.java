package com.game.server.mappers;

import com.game.server.bean.PlayerScene;
import com.game.server.constant.SqlCmdConstant;
import core.annotation.SqlCmd;
import core.sql.SqlConstant;
import org.apache.ibatis.annotations.*;

public interface PlayerSceneMapper {

    @SqlCmd(sqlCmd = SqlCmdConstant.PLAYER_SCENE_GET_SCENE_INFO, sqlType = SqlConstant.SELECT_ONE)
    @Select("select * from game_player_scene where player_index = #{playerIndex}")
    public PlayerScene getSceneInfoByPlayerIndex(@Param("playerIndex") int playerIndex);

    @SqlCmd(sqlCmd = SqlCmdConstant.PLAYER_SCENE_UPDATE_SCENE_INFO, sqlType = SqlConstant.UPDATE)
    @Update("update game_player_scene set " +
            "player_position_x = #{playerPositionX}," +
            "player_position_y = #{playerPositionY}," +
            "scene_id = #{sceneId} " +
            "where player_index = #{playerIndex}")
    public void updateSceneByPlayerIndex(PlayerScene playerScene);


    @SqlCmd(sqlCmd = SqlCmdConstant.PLAYER_SCENE_INSERT_SCENE_INFO, sqlType = SqlConstant.INSERT)
    @Insert("INSERT INTO game_player_scene (player_index, player_position_x, player_position_y, scene_id) " +
            "VALUES (#{playerIndex}, #{playerPositionX}, #{playerPositionY}, #{sceneId})")
    public void insertPlayerSceneInfo(PlayerScene playerBean);


    @SqlCmd(sqlCmd = SqlCmdConstant.PLAYER_SCENE_DELETE_SCENE_INFO, sqlType = SqlConstant.DELETE)
    @Insert("delete from game_player_scene where player_index = #{playerIndex}")
    public void deletePlayerSceneInfo(@Param("playerIndex") int playerIndex);

}
