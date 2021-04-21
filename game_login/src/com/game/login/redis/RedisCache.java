package com.game.login.redis;


import bean.login.PlayerLoginBean;
import bean.login.NoticeBean;
import bean.login.ServerListInfoBean;
import core.annotation.SqlAnnotation;
import core.redis.RedisManager;
import core.sql.SqlDao;
import org.apache.ibatis.jdbc.SQL;
import org.nutz.dao.Cnd;
import org.nutz.dao.pager.Pager;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;

import java.util.List;

import static com.game.login.constant.RedisConstant.*;
import static com.game.login.constant.SqlCmdConstant.NOTICE_LIST_SELECT_ALL;
import static com.game.login.constant.SqlCmdConstant.PLAYER_INFO_SELECT_LAST_LOGIN;
import static com.game.login.constant.SqlCmdConstant.SERVER_LIST_SELECT_ALL;
import static core.Constants.SQL_MASTER;

public class RedisCache {

    private static class DefaultInstance {
        static final RedisCache INSTANCE = new RedisCache();
    }

    public static RedisCache getInstance() {
        return DefaultInstance.INSTANCE;
    }

    private RMap<Integer, ServerListInfoBean> serverListCache;
    private RMap<Integer, NoticeBean> noticeListCache;
    private RMap<String, PlayerLoginBean> playerInfoCache; //key openId

    private RedisCache() {
        loadData();
    }

    private void loadData() {
        loadServerListMap();
        loadServerNoticeMap();
        loadPlayInfoMap();
    }

    private void loadServerListMap() {
        RedissonClient redissonClient = RedisManager.getInstance().getRedisSon();
        serverListCache = redissonClient.getMapCache(REDIS_SERVER_LIST_KEY);
        serverListCache.clear();
        List<ServerListInfoBean> allServerList = SqlDao.getInstance().getDao(SQL_MASTER).query(ServerListInfoBean.class, null);
        for (int i = 0; i < allServerList.size(); i++) {
            ServerListInfoBean serverInfoBean = allServerList.get(i);
            serverListCache.put(serverInfoBean.getServerId(), serverInfoBean);
        }
    }

    private void loadServerNoticeMap() {
        RedissonClient redissonClient = RedisManager.getInstance().getRedisSon();
        noticeListCache = redissonClient.getMapCache(REDIS_SERVER_NOTICE_KEY);
        noticeListCache.clear();
        List<NoticeBean> allNoticeList = SqlDao.getInstance().getDao(SQL_MASTER).query(NoticeBean.class, null);
        for (int i = 0; i < allNoticeList.size(); i++) {
            NoticeBean noticeBean = allNoticeList.get(i);
            noticeListCache.put(noticeBean.getNoticeId(), noticeBean);
        }
    }

    private void loadPlayInfoMap() {
        RedissonClient redissonClient = RedisManager.getInstance().getRedisSon();
        playerInfoCache = redissonClient.getMapCache(REDIS_PLAYER_INFO_LIST);
        playerInfoCache.clear();
        List<PlayerLoginBean> lastLoginPlayerList = SqlDao.getInstance().getDao(SQL_MASTER).query(PlayerLoginBean.class,
                Cnd.orderBy().desc("login_time"), new Pager(1, 5000));
        for (int i = 0; i < lastLoginPlayerList.size(); i++) {
            PlayerLoginBean loginPlayerBean = lastLoginPlayerList.get(i);
            playerInfoCache.put(loginPlayerBean.getOpenId(), loginPlayerBean);
        }
    }

    public RMap<Integer, ServerListInfoBean> getServerListCache() {
        return serverListCache;
    }

    public RMap<Integer, NoticeBean> getNoticeListCache() {
        return noticeListCache;
    }

    public RMap<String, PlayerLoginBean> getPlayerInfoCache() {
        return playerInfoCache;
    }
}
