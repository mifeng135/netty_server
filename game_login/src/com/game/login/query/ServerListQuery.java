package com.game.login.query;

import bean.login.ServerListInfoBean;
import com.game.login.redis.RedisCache;
import core.redis.RedisManager;
import core.sql.SqlDao;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.redisson.api.RMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.game.login.constant.RedisConstant.REDIS_SERVER_LIST_KEY;


public class ServerListQuery {

    /**
     * 查询所有服务器列表
     *
     * @return
     */
    public static List<ServerListInfoBean> queryAllServerList() {
        RMap<Integer, ServerListInfoBean> redisCache = RedisManager.getInstance().getMap(REDIS_SERVER_LIST_KEY);
        return new ArrayList<>(redisCache.values());
    }

    /**
     * 更新服务器名称
     *
     * @param serverId
     * @param serverName
     * @return 0 fail
     */
    public static boolean updateServerName(int serverId, String serverName) {
        boolean result = SqlDao.getInstance().getDao().update(ServerListInfoBean.class,
                Chain.make("server_name", serverName),
                Cnd.where("server_id", "=", serverId)) > 0;

        if (result) {
            RMap<Integer, ServerListInfoBean> serverListCache = RedisCache.getInstance().getServerListCache();
            ServerListInfoBean redisData = serverListCache.get(serverId);
            redisData.setServerName(serverName);
            serverListCache.put(serverId, redisData);
        }
        return result;
    }

    /**
     * 通过服务器id 更新服务器状态
     *
     * @param serverId
     * @param state
     * @return
     */
    public static boolean updateServerState(int serverId, int state) {
        boolean result = SqlDao.getInstance().getDao().update(ServerListInfoBean.class,
                Chain.make("state", state),
                Cnd.where("server_id", "=", serverId)) > 0;
        if (result) {
            RMap<Integer, ServerListInfoBean> serverListCache = RedisCache.getInstance().getServerListCache();
            ServerListInfoBean redisData = serverListCache.get(serverId);
            redisData.setState(state);
            serverListCache.put(serverId, redisData);
        }
        return result;
    }

    /**
     * 通过serverid 删除一个服务器
     *
     * @param serverId
     * @return
     */
    public static boolean deleteServer(int serverId) {
        boolean result = SqlDao.getInstance().getDao().clear(ServerListInfoBean.class, Cnd.where("server_id", "=", serverId)) > 0;
        if (result) {
            RedisCache.getInstance().getServerListCache().remove(serverId);
        }
        return result;
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
    public static boolean insertServer(String serverName, int serverId, int state, int openTime, String serverIp) {
        ServerListInfoBean serverListBean = new ServerListInfoBean();
        serverListBean.setServerName(serverName);
        serverListBean.setServerId(serverId);
        serverListBean.setState(state);
        serverListBean.setServerIp(serverIp);
        serverListBean.setOpenTime(openTime);
        serverListBean = SqlDao.getInstance().getDao().insert(serverListBean);
        if (serverListBean != null) {
            RedisCache.getInstance().getServerListCache().put(serverId, serverListBean);
            return true;
        }
        return false;
    }

    /***
     * 更新服务器ip
     * @param serverId
     * @param serverIp
     * @return
     */
    public static boolean updateServerIp(int serverId, String serverIp) {
        boolean result = SqlDao.getInstance().getDao().
                update(ServerListInfoBean.class,
                        Chain.make("server_ip", serverIp).add("state", 2),
                        Cnd.where("server_id", "=", serverId)) > 0;
//        if (result) {
//            RMap<Integer, ServerListInfoBean> serverListCache = RedisCache.getInstance().getServerListCache();
//            ServerListInfoBean redisData = serverListCache.get(serverId);
//            redisData.setServerIp(serverIp);
//            serverListCache.put(serverId, redisData);
//        }
        return result;
    }
}
