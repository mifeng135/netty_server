package com.game.login.redis;


import bean.login.LoginPlayerInfoBean;
import bean.login.LoginNoticeBean;
import bean.login.ServerListInfoBean;
import bean.login.LoginPlayerServerInfoBean;
import com.game.login.ProperticeConfig;
import core.redis.RedisManager;
import core.sql.SqlDao;
import org.nutz.dao.Cnd;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Sql;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.game.login.constant.RedisConstant.*;

public class RedisCache {

    private static class DefaultInstance {
        static final RedisCache INSTANCE = new RedisCache();
    }

    public static RedisCache getInstance() {
        return DefaultInstance.INSTANCE;
    }

    private RMap<Integer, ServerListInfoBean> serverListCache;
    private RMap<Integer, LoginNoticeBean> noticeListCache;
    private RMap<String, LoginPlayerInfoBean> playerInfoCache; //key openId
    private RMap<Integer, List<LoginPlayerServerInfoBean>> playerServerInfoCache;

    private RedisCache() {
        loadData();
    }

    private void loadData() {
        loadServerListMap();
        loadServerNoticeMap();
        loadPlayInfoMap();
        loadPlayerServerInfoMap();
    }

    private void loadServerListMap() {
        RedissonClient redissonClient = RedisManager.getInstance().getRedisSon();
        serverListCache = redissonClient.getMapCache(REDIS_SERVER_LIST_KEY);
        serverListCache.clear();
        List<ServerListInfoBean> allServerList = SqlDao.getInstance().getDao().query(ServerListInfoBean.class, null);
        for (int i = 0; i < allServerList.size(); i++) {
            ServerListInfoBean serverInfoBean = allServerList.get(i);
            serverListCache.put(serverInfoBean.getServerId(), serverInfoBean);
        }
    }

    private void loadServerNoticeMap() {
        RedissonClient redissonClient = RedisManager.getInstance().getRedisSon();
        noticeListCache = redissonClient.getMapCache(REDIS_SERVER_NOTICE_KEY);
        noticeListCache.clear();
        List<LoginNoticeBean> allNoticeList = SqlDao.getInstance().getDao().query(LoginNoticeBean.class, null);
        for (int i = 0; i < allNoticeList.size(); i++) {
            LoginNoticeBean noticeBean = allNoticeList.get(i);
            noticeListCache.put(noticeBean.getNoticeId(), noticeBean);
        }
    }

    private void loadPlayInfoMap() {
        RedissonClient redissonClient = RedisManager.getInstance().getRedisSon();
        playerInfoCache = redissonClient.getMapCache(REDIS_PLAYER_OPEN_INFO_LIST);
        playerInfoCache.clear();
        List<LoginPlayerInfoBean> lastLoginPlayerList = SqlDao.getInstance().getDao().
                query(LoginPlayerInfoBean.class,
                        Cnd.orderBy().desc("login_time"),
                        new Pager(1, 5000));
        for (int i = 0; i < lastLoginPlayerList.size(); i++) {
            LoginPlayerInfoBean loginPlayerBean = lastLoginPlayerList.get(i);
            playerInfoCache.put(loginPlayerBean.getOpenId(), loginPlayerBean);
        }
    }

    private void loadPlayerServerInfoMap() {
        RedissonClient redissonClient = RedisManager.getInstance().getRedisSon();
        playerServerInfoCache = redissonClient.getMapCache(REDIS_PLAYER_SERVER_INFO);
        playerServerInfoCache.clear();

        List<LoginPlayerInfoBean> lastLoginPlayerList = SqlDao.getInstance().getDao().
                query(LoginPlayerInfoBean.class,
                        Cnd.orderBy().desc("login_time"),
                        new Pager(0, ProperticeConfig.redisPlayerCacheCount));

        List<Integer> playerIndexList = lastLoginPlayerList.stream().map(LoginPlayerInfoBean::getPlayerIndex).collect(Collectors.toList());

        Sql sql = SqlDao.getInstance().getDao().sqls().create("select_player_login_info.data");
        sql.setParam("value", playerIndexList);
        sql.setCallback((conn, rs, sql1) -> {
            List<LoginPlayerServerInfoBean> list = new LinkedList<>();
            while (rs.next()) {
                LoginPlayerServerInfoBean playerServerInfoBean = SqlDao.getInstance().getDao().
                        getEntity(LoginPlayerServerInfoBean.class).
                        getObject(rs, null, "");
                list.add(playerServerInfoBean);
            }
            return list;
        });
        SqlDao.getInstance().getDao().execute(sql);
        List<LoginPlayerServerInfoBean> playerServerInfoBeanList = sql.getList(LoginPlayerServerInfoBean.class);
        Map<Integer, List<LoginPlayerServerInfoBean>> playerMapInfo = playerServerInfoBeanList.stream().
                collect(Collectors.groupingBy(LoginPlayerServerInfoBean::getPlayerIndex));
        playerServerInfoCache.putAll(playerMapInfo);
    }

    public RMap<Integer, ServerListInfoBean> getServerListCache() {
        return serverListCache;
    }

    public RMap<Integer, LoginNoticeBean> getNoticeListCache() {
        return noticeListCache;
    }

    public RMap<String, LoginPlayerInfoBean> getPlayerInfoCache() {
        return playerInfoCache;
    }

    public RMap<Integer, List<LoginPlayerServerInfoBean>> getPlayerServerInfoCache() {
        return playerServerInfoCache;
    }
}
