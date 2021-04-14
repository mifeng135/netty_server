package com.game.server.query;

import bean.login.ServerInfoBean;
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
    public static List<ServerInfoBean> queryAllServerList() {
        RMap<Integer, ServerInfoBean> redisCache = RedisCache.getInstance().getServerListCache();
        Collection<ServerInfoBean> valueCollection = redisCache.values();
        return new ArrayList<>(valueCollection);
    }

    /**
     * 更新服务器名称
     *
     * @param serverId
     * @param serverName
     * @return 0 fail
     */
    public static int updateServerName(int serverId, String serverName) {
        ServerInfoBean serverListBean = new ServerInfoBean();
        serverListBean.setServerId(serverId);
        serverListBean.setServerName(serverName);
        RMap<Integer, ServerInfoBean> serverListCache = RedisCache.getInstance().getServerListCache();
        ServerInfoBean redisData = serverListCache.get(serverId);
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
        ServerInfoBean serverListBean = new ServerInfoBean();
        serverListBean.setServerId(serverId);
        serverListBean.setState(state);

        RMap<Integer, ServerInfoBean> serverListCache = RedisCache.getInstance().getServerListCache();
        ServerInfoBean redisData = serverListCache.get(serverId);
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
        ServerInfoBean serverListBean = new ServerInfoBean();
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
        ServerInfoBean serverListBean = new ServerInfoBean();
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
        ServerInfoBean serverListBean = new ServerInfoBean();
        serverListBean.setServerId(serverId);
        serverListBean.setServerIp(serverIp);

        RMap<Integer, ServerInfoBean> serverListCache = RedisCache.getInstance().getServerListCache();
        ServerInfoBean redisData = serverListCache.get(serverId);
        redisData.setServerIp(serverIp);
        serverListCache.put(serverId, redisData);

        return SqlAnnotation.getInstance().executeCommitSql(SERVER_LIST_UPDATE_SERVER_IP, serverListBean);
    }
}
