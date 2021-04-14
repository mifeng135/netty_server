package com.game.server.query;

import bean.login.NoticeBean;
import com.game.server.redis.RedisCache;
import core.annotation.SqlAnnotation;
import org.redisson.api.RMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.game.server.constant.SqlCmdConstant.NOTICE_DELETE;
import static com.game.server.constant.SqlCmdConstant.NOTICE_INSERT;
import static com.game.server.constant.SqlCmdConstant.NOTICE_UPDATE_CONTENT;
import static core.Constants.SQL_RESULT_SUCCESS;

public class NoticeListQuery {

    public static List<NoticeBean> getAllNotice() {
        RMap<Integer, NoticeBean> redisCache = RedisCache.getInstance().getNoticeListCache();
        Collection<NoticeBean> valueCollection = redisCache.values();
        return new ArrayList<>(valueCollection);
    }

    public static int updateNoticeContent(int noticeId, String content) {
        RMap<Integer, NoticeBean> redisCache = RedisCache.getInstance().getNoticeListCache();
        NoticeBean noticeBean = redisCache.get(noticeId);
        noticeBean.setContent(content);
        int result = SqlAnnotation.getInstance().executeCommitSql(NOTICE_UPDATE_CONTENT, noticeBean);
        if (result == SQL_RESULT_SUCCESS) {
            redisCache.put(noticeId, noticeBean);
        }
        return result;
    }

    public static int deleteNotice(int noticeId) {
        RMap<Integer, NoticeBean> redisCache = RedisCache.getInstance().getNoticeListCache();
        int result = SqlAnnotation.getInstance().executeCommitSql(NOTICE_DELETE, noticeId);
        if (result == SQL_RESULT_SUCCESS) {
            redisCache.remove(noticeId);
        }
        return result;
    }

    public static int insertNotice(String content) {
        NoticeBean noticeBean = new NoticeBean();
        noticeBean.setContent(content);
        int result = SqlAnnotation.getInstance().executeCommitSql(NOTICE_INSERT, noticeBean);
        if (result == SQL_RESULT_SUCCESS) {
            RMap<Integer, NoticeBean> redisCache = RedisCache.getInstance().getNoticeListCache();
            redisCache.put(noticeBean.getNoticeId(), noticeBean);
        }
        return result;
    }
}
