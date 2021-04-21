package com.game.login.query;

import bean.login.NoticeBean;
import com.game.login.redis.RedisCache;
import core.annotation.SqlAnnotation;
import core.sql.SqlDao;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.sql.Sql;
import org.redisson.api.RMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static core.Constants.SQL_MASTER;
import static core.Constants.SQL_RESULT_FAIL;
import static core.Constants.SQL_RESULT_SUCCESS;

public class NoticeListQuery {

    public static List<NoticeBean> getAllNotice() {
        RMap<Integer, NoticeBean> redisCache = RedisCache.getInstance().getNoticeListCache();
        Collection<NoticeBean> valueCollection = redisCache.values();
        return new ArrayList<>(valueCollection);
    }

    public static int updateNoticeContent(int noticeId, String content) {
        RMap<Integer, NoticeBean> redisCache = RedisCache.getInstance().getNoticeListCache();
        int result = SqlDao.getInstance().getDao(SQL_MASTER).update(NoticeBean.class,
                Chain.make("content", content),
                Cnd.where("notice_id", "=", noticeId));
        if (result == SQL_RESULT_SUCCESS) {
            NoticeBean noticeBean = new NoticeBean();
            noticeBean.setContent(content);
            noticeBean.setNoticeId(noticeId);
            redisCache.put(noticeId, noticeBean);
        }
        return result;
    }

    public static int deleteNotice(int noticeId) {
        RMap<Integer, NoticeBean> redisCache = RedisCache.getInstance().getNoticeListCache();
        int result = SqlDao.getInstance().getDao(SQL_MASTER).delete(NoticeBean.class, noticeId);
        if (result == SQL_RESULT_SUCCESS) {
            redisCache.remove(noticeId);
        }
        return result;
    }

    public static int insertNotice(String content) {
        NoticeBean noticeBean = new NoticeBean();
        noticeBean.setContent(content);
        noticeBean = SqlDao.getInstance().getDao(SQL_MASTER).insert(noticeBean);
        if (noticeBean != null) {
            RMap<Integer, NoticeBean> redisCache = RedisCache.getInstance().getNoticeListCache();
            redisCache.put(noticeBean.getNoticeId(), noticeBean);
            return SQL_RESULT_SUCCESS;
        }
        return SQL_RESULT_FAIL;
    }
}
