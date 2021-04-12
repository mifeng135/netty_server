package com.game.server.query;

import com.game.server.bean.ServerListBean;
import com.game.server.redis.RedisCache;
import core.annotation.SqlAnnotation;
import org.redisson.api.RMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.game.server.constant.SqlCmdConstant.*;

public class ServerListQuery {

    /**
     * 查询所有服务器列表
     *
     * @return
     */
    public static List<ServerListBean> queryAllServerList() {
        RMap<Integer, ServerListBean> redisCache = RedisCache.getInstance().getServerListCache();
        if (redisCache != null && redisCache.size() > 0) {
            Collection<ServerListBean> valueCollection = redisCache.values();
            return new ArrayList<>(valueCollection);
        }
        return SqlAnnotation.getInstance().sqlSelectList(SERVER_LIST_SELECT_ALL);
    }

    /**
     * 更新服务器名称
     *
     * @param serverId
     * @param serverName
     * @return 0 fail
     */
    public static int updateServerName(int serverId, String serverName) {
        ServerListBean serverListBean = new ServerListBean();
        serverListBean.setServerId(serverId);
        serverListBean.setServerName(serverName);
        RMap<Integer, ServerListBean> serverListCache = RedisCache.getInstance().getServerListCache();
        ServerListBean redisData = serverListCache.get(serverId);
        redisData.setServerName(serverName);
        serverListCache.put(serverId, redisData);
        return SqlAnnotation.getInstance().executeCommitSql(SERVER_LIST_UPDATE_SERVER_NAME, serverListBean);
    }

    /**
     * 通过服务器id 更新服务器状态
     *
     * @param serverId
     * @param state
     * @return
     */
    public static int updateServerState(int serverId, int state) {
        ServerListBean serverListBean = new ServerListBean();
        serverListBean.setServerId(serverId);
        serverListBean.setState(state);

        RMap<Integer, ServerListBean> serverListCache = RedisCache.getInstance().getServerListCache();
        ServerListBean redisData = serverListCache.get(serverId);
        redisData.setState(state);
        serverListCache.put(serverId, redisData);
        return SqlAnnotation.getInstance().executeCommitSql(SERVER_LIST_UPDATE_SERVER_STATE, serverListBean);
    }

    /**
     * 通过serverid 删除一个服务器
     *
     * @param serverId
     * @return
     */
    public static int deleteServer(int serverId) {
        ServerListBean serverListBean = new ServerListBean();
        serverListBean.setServerId(serverId);
        RedisCache.getInstance().getServerListCache().remove(serverId);
        return SqlAnnotation.getInstance().executeCommitSql(SERVER_LIST_DELETE_SERVER, serverListBean);
    }

    /**
     * 插入服务器
     *
     * @param serverName
     * @param serverId
     * @param state
     * @param openTime
     * @return
     */
    public static int insertServer(String serverName, int serverId, int state, int openTime) {
        ServerListBean serverListBean = new ServerListBean();
        serverListBean.setServerName(serverName);
        serverListBean.setServerId(serverId);
        serverListBean.setState(state);
        serverListBean.setOpenTime(openTime);
        RedisCache.getInstance().getServerListCache().put(serverId,serverListBean);
        return SqlAnnotation.getInstance().executeCommitSql(SERVER_LIST_INSERT_SERVER, serverListBean);
    }

    /***
     * 更新服务器ip
     * @param serverId
     * @param serverIp
     * @return
     */
    public static int updateServerIp(int serverId, String serverIp) {
        ServerListBean serverListBean = new ServerListBean();
        serverListBean.setServerId(serverId);
        serverListBean.setServerIp(serverIp);

        RMap<Integer, ServerListBean> serverListCache = RedisCache.getInstance().getServerListCache();
        ServerListBean redisData = serverListCache.get(serverId);
        redisData.setServerIp(serverIp);
        serverListCache.put(serverId, redisData);

        return SqlAnnotation.getInstance().executeCommitSql(SERVER_LIST_UPDATE_SERVER_IP, serverListBean);
    }
}
