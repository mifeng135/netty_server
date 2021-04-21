package com.game.login.query;

import bean.login.NoticeBean;
import bean.login.ServerListInfoBean;
import com.game.login.redis.RedisCache;
import core.annotation.SqlAnnotation;
import core.sql.SqlDao;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.redisson.api.RMap;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.game.login.constant.SqlCmdConstant.*;
import static core.Constants.SQL_MASTER;
import static core.Constants.SQL_RESULT_FAIL;
import static core.Constants.SQL_RESULT_SUCCESS;

public class ServerListQuery {

    /**
     * 查询所有服务器列表
     *
     * @return
     */
    public static List<ServerListInfoBean> queryAllServerList() {
        RMap<Integer, ServerListInfoBean> redisCache = RedisCache.getInstance().getServerListCache();
        Collection<ServerListInfoBean> valueCollection = redisCache.values();
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
        int result = SqlDao.getInstance().getDao(SQL_MASTER).update(ServerListInfoBean.class,
                Chain.make("server_name", serverName),
                Cnd.where("server_id", "=", serverId));

        if (result == SQL_RESULT_SUCCESS) {
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
    public static int updateServerState(int serverId, int state) {
        int result = SqlDao.getInstance().getDao(SQL_MASTER).update(ServerListInfoBean.class,
                Chain.make("state", state),
                Cnd.where("server_id", "=", serverId));
        if (result == SQL_RESULT_SUCCESS) {
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
    public static int deleteServer(int serverId) {
        Sql sql = Sqls.create("delete from game_server_list");
        sql = SqlDao.getInstance().getDao(SQL_MASTER).execute(sql);
        int result = sql.getUpdateCount();
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
    public static int insertServer(String serverName, int serverId, int state, int openTime,String serverIp) {
        ServerListInfoBean serverListBean = new ServerListInfoBean();
        serverListBean.setServerName(serverName);
        serverListBean.setServerId(serverId);
        serverListBean.setState(state);
        serverListBean.setServerIp(serverIp);
        serverListBean.setOpenTime(openTime);
        serverListBean = SqlDao.getInstance().getDao(SQL_MASTER).insert(serverListBean);
        if (serverListBean != null) {
            RedisCache.getInstance().getServerListCache().put(serverId, serverListBean);
            return SQL_RESULT_SUCCESS;
        }
        return SQL_RESULT_FAIL;
    }

    /***
     * 更新服务器ip
     * @param serverId
     * @param serverIp
     * @return
     */
    public static int updateServerIp(int serverId, String serverIp) {
        ServerListInfoBean serverListBean = new ServerListInfoBean();
        serverListBean.setServerId(serverId);
        serverListBean.setServerIp(serverIp);

        int result = SqlDao.getInstance().getDao(SQL_MASTER).update(ServerListInfoBean.class,
                Chain.make("server_ip", serverIp),
                Cnd.where("server_id", "=", serverId));
        if (result == SQL_RESULT_SUCCESS) {
            RMap<Integer, ServerListInfoBean> serverListCache = RedisCache.getInstance().getServerListCache();
            ServerListInfoBean redisData = serverListCache.get(serverId);
            redisData.setServerIp(serverIp);
            serverListCache.put(serverId, redisData);
        }
        return result;
    }
}
