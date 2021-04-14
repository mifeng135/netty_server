package com.game.server.mappers;


import bean.player.PlayerItem;
import com.game.server.constant.SqlCmdConstant;
import core.annotation.SqlCmd;
import core.sql.SqlConstant;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface PlayerItemMapper {

    @SqlCmd(sqlCmd = SqlCmdConstant.PLAYER_ITEM_SELECT_ALL_ITEM, sqlType = SqlConstant.SELECT_LIST)
    @Select("select item_id,item_count from game_player_item where player_index = #{playerIndex}")
    List<PlayerItem> getPlayerAllItem(PlayerItem playerItem);


    @SqlCmd(sqlCmd = SqlCmdConstant.PLAYER_ITEM_SELECT_ONE_ITEM, sqlType = SqlConstant.SELECT_ONE)
    @Select("select item_id,item_count from game_player_item where player_index = #{playerIndex} and item_id = #{itemId}")
    PlayerItem getPlayerOneItem(PlayerItem playerItem);


    @SqlCmd(sqlCmd = SqlCmdConstant.PLAYER_ITEM_UPDATE_ITEM, sqlType = SqlConstant.UPDATE)
    @Update("update game_player_item set item_count = #{itemCount} where player_index = #{playerIndex} and item_id = #{itemId}")
    void updatePlayerItem(PlayerItem playerItem);


    @SqlCmd(sqlCmd = SqlCmdConstant.PLAYER_ITEM_INSERT_ITEM, sqlType = SqlConstant.INSERT)
    @Update("INSERT INTO game_player_item (player_index, item_id, item_count)" +
            "VALUES (#{playerIndex}, #{itemId}, #{itemCount})")
    void insertPlayerItem(PlayerItem playerItem);


    @SqlCmd(sqlCmd = SqlCmdConstant.PLAYER_ITEM_DELETE_ITEM, sqlType = SqlConstant.DELETE)
    @Delete("delete from game_player_item where player_index = #{playerIndex} and item_id = #{itemId}")
    void deletePlayerItem(PlayerItem playerItem);
}


