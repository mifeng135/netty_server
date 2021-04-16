package com.game.login.mappers;


import bean.login.ServerInfoBean;
import com.game.login.constant.SqlCmdConstant;
import core.annotation.SqlCmd;
import core.sql.SqlConstant;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface ServerListMapper {

    @SqlCmd(sqlCmd = SqlCmdConstant.SERVER_LIST_SELECT_ALL, sqlType = SqlConstant.SELECT_LIST)
    @Select("select server_name, server_id, state, open_time, server_ip from game_server_list")
    List<ServerInfoBean> getAllServerList();

    @SqlCmd(sqlCmd = SqlCmdConstant.SERVER_LIST_SELECT_ONE, sqlType = SqlConstant.SELECT_ONE)
    @Select("select server_name, server_id, state, open_time, server_ip from game_server_list where server_id = #{serverId} limit 1")
    ServerInfoBean getServerById(ServerInfoBean serverListBean);

    @SqlCmd(sqlCmd = SqlCmdConstant.SERVER_LIST_UPDATE_SERVER_NAME, sqlType = SqlConstant.UPDATE)
    @Update("update game_server_list set server_name = #{serverName} where server_id = #{serverId}")
    void updateServerName(ServerInfoBean serverListBean);

    @SqlCmd(sqlCmd = SqlCmdConstant.SERVER_LIST_UPDATE_SERVER_STATE, sqlType = SqlConstant.UPDATE)
    @Update("update game_server_list set state = #{state} where server_id = #{serverId}")
    void updateServerState(ServerInfoBean serverListBean);

    @SqlCmd(sqlCmd = SqlCmdConstant.SERVER_LIST_DELETE_SERVER, sqlType = SqlConstant.DELETE)
    @Delete("delete from game_server_list where server_id = #{serverId}")
    void deleteServer(ServerInfoBean serverListBean);

    @SqlCmd(sqlCmd = SqlCmdConstant.SERVER_LIST_INSERT_SERVER, sqlType = SqlConstant.INSERT)
    @Insert("INSERT INTO game_server_list (server_name, server_id, state, open_time)" +
            "VALUES (#{serverName}, #{serverId}, #{state}, #{openTime})")
    void insertServer(ServerInfoBean serverListBean);

    @SqlCmd(sqlCmd = SqlCmdConstant.SERVER_LIST_UPDATE_SERVER_IP, sqlType = SqlConstant.UPDATE)
    @Update("update game_server_list set server_ip = #{serverIp} where server_id = #{serverId}")
    void updateServerIp(ServerInfoBean serverListBean);
}
