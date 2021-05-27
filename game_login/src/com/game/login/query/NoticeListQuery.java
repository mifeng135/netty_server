package com.game.login.query;

import bean.login.LoginNoticeBean;
import com.game.login.redis.RedisCache;
import core.annotation.Query;
import core.annotation.QueryCtrl;
import core.sql.SqlDao;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.redisson.api.RMap;

import java.util.ArrayList;
import java.util.List;


@QueryCtrl
public class NoticeListQuery {

    @Query(cmd = 1)
    public List<LoginNoticeBean> getAllNotice() {
        RMap<Integer, LoginNoticeBean> redisCache = RedisCache.getInstance().getNoticeListCache();
        return new ArrayList<>(redisCache.values());
    }

    public static boolean updateNoticeContent(int noticeId, String content) {
        boolean result = SqlDao.getInstance().getDao().update(LoginNoticeBean.class,
                Chain.make("content", content),
                Cnd.where("notice_id", "=", noticeId)) > 0;
        if (result) {
            LoginNoticeBean noticeBean = new LoginNoticeBean();
            noticeBean.setContent(content);
            noticeBean.setNoticeId(noticeId);
            RMap<Integer, LoginNoticeBean> redisCache = RedisCache.getInstance().getNoticeListCache();
            redisCache.put(noticeId, noticeBean);
        }
        return result;
    }

    public static boolean deleteNotice(int noticeId) {
        boolean success = SqlDao.getInstance().getDao().clear(LoginNoticeBean.class,
                Cnd.where("notice_id", "=", noticeId)) > 0;
        if (success) {
            RMap<Integer, LoginNoticeBean> redisCache = RedisCache.getInstance().getNoticeListCache();
            redisCache.remove(noticeId);
        }
        return success;
    }

    public static boolean insertNotice(String content) {
        LoginNoticeBean noticeBean = new LoginNoticeBean();
        noticeBean.setContent(content);
        noticeBean = SqlDao.getInstance().getDao().insert(noticeBean);
        if (noticeBean != null) {
            RMap<Integer, LoginNoticeBean> redisCache = RedisCache.getInstance().getNoticeListCache();
            redisCache.put(noticeBean.getNoticeId(), noticeBean);
            return true;
        }
        return false;
    }
}
