package com.game.server.query;

import com.game.server.bean.NoticeBean;
import com.game.server.redis.RedisCache;
import org.redisson.api.RMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NoticeListQuery {

    public static List<NoticeBean> getAllNotice() {
        RMap<Integer, NoticeBean> redisCache = RedisCache.getInstance().getNoticeListCache();
        Collection<NoticeBean> valueCollection = redisCache.values();
        return new ArrayList<>(valueCollection);
    }
}
