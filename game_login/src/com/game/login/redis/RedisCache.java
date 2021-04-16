package com.game.login.redis;


import bean.login.LoginPlayerBean;
import bean.login.NoticeBean;
import bean.login.ServerInfoBean;
import core.annotation.SqlAnnotation;
import core.redis.RedisManager;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;

import java.util.List;

import static com.game.login.constant.RedisConstant.*;
import static com.game.login.constant.SqlCmdConstant.NOTICE_LIST_SELECT_ALL;
import static com.game.login.constant.SqlCmdConstant.PLAYER_INFO_SELECT_LAST_LOGIN;
import static com.game.login.constant.SqlCmdConstant.SERVER_LIST_SELECT_ALL;

public class RedisCache {

    private static class DefaultInstance {
        static final RedisCache INSTANCE = new RedisCache();
    }

    public static RedisCache getInstance() {
        return DefaultInstance.INSTANCE;
    }

    private RMap<Integer, ServerInfoBean> serverListCache;
    private RMap<Integer, NoticeBean> noticeListCache;
    private RMap<String, LoginPlayerBean> playerInfoCache; //key openId

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
        List<ServerInfoBean> allServerList = SqlAnnotation.getInstance().sqlSelectList(SERVER_LIST_SELECT_ALL);
        for (int i = 0; i < allServerList.size(); i++) {
            ServerInfoBean serverInfoBean = allServerList.get(i);
            serverListCache.put(serverInfoBean.getServerId(), serverInfoBean);
        }
    }

    private void loadServerNoticeMap() {
        RedissonClient redissonClient = RedisManager.getInstance().getRedisSon();
        noticeListCache = redissonClient.getMapCache(REDIS_SERVER_NOTICE_KEY);
        List<NoticeBean> allNoticeList = SqlAnnotation.getInstance().sqlSelectList(NOTICE_LIST_SELECT_ALL);
        for (int i = 0; i < allNoticeList.size(); i++) {
            NoticeBean noticeBean = allNoticeList.get(i);
            noticeListCache.put(noticeBean.getNoticeId(), noticeBean);
        }
    }

    private void loadPlayInfoMap() {
        RedissonClient redissonClient = RedisManager.getInstance().getRedisSon();
        playerInfoCache = redissonClient.getMapCache(REDIS_PLAYER_INFO_LIST);

        List<LoginPlayerBean> lastLoginPlayerList = SqlAnnotation.getInstance().sqlSelectList(PLAYER_INFO_SELECT_LAST_LOGIN);
        for (int i = 0; i < lastLoginPlayerList.size(); i++) {
            LoginPlayerBean loginPlayerBean = lastLoginPlayerList.get(i);
            playerInfoCache.put(loginPlayerBean.getOpenId(), loginPlayerBean);
        }
    }

    public RMap<Integer, ServerInfoBean> getServerListCache() {
        return serverListCache;
    }

    public RMap<Integer, NoticeBean> getNoticeListCache() {
        return noticeListCache;
    }

    public RMap<String, LoginPlayerBean> getPlayerInfoCache() {
        return playerInfoCache;
    }
}
