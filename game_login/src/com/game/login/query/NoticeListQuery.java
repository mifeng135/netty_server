package com.game.login.query;

import bean.login.LoginNoticeBean;
import bean.login.LoginPlayerInfoBean;
import com.game.login.TestMap;
import com.game.login.redis.RedisCache;
import core.annotation.Query;
import core.annotation.QueryCtrl;
import core.sql.SqlDao;
import core.util.Instance;
import core.util.ProtoUtil;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.redisson.api.RMap;

import java.util.ArrayList;
import java.util.List;

import static constants.RedisConstant.*;
import static constants.SqlConstant.*;


@QueryCtrl
public class NoticeListQuery {

    @Query(cmd = QUERY_ALL_NOTICE)
    public List<LoginNoticeBean> getAllNotice() {
        RMap<Integer, LoginNoticeBean> redisCache = RedisCache.getInstance().getNoticeListCache();
        return new ArrayList<>(redisCache.values());
    }

    @Query(cmd = UPDATE_NOTICE)
    public boolean updateNoticeContent(int noticeId, String content) {
        boolean result = Instance.sql().update(LoginNoticeBean.class,
                Chain.make("content", content),
                Cnd.where("notice_id", "=", noticeId)) > 0;

        return result;
    }

    @Query(cmd = DELETE_NOTICE)
    public boolean deleteNotice(int noticeId) {
        boolean success = Instance.sql().clear(LoginNoticeBean.class, Cnd.where("notice_id", "=", noticeId)) > 0;
        if (success) {
            Instance.redis().mapFastRemove(REDIS_SERVER_NOTICE_KEY, noticeId);
        }
        return success;
    }

    @Query(cmd = INSERT_NOTICE)
    public boolean insertNotice(String content) {
        LoginNoticeBean noticeBean = new LoginNoticeBean();
        noticeBean.setContent(content);
        noticeBean = SqlDao.getInstance().getDao().insert(noticeBean);
        if (noticeBean != null) {
            Instance.redis().mapPut(REDIS_SERVER_NOTICE_KEY, noticeBean.getNoticeId(), noticeBean);
            return true;
        }
        return false;
    }

    @Query(cmd = NOTICE_TEST)
    public boolean updateNoticeContent(int noticeId, byte[] data) {
        boolean result = Instance.sql().update(LoginNoticeBean.class,
                Chain.make("item", data),
                Cnd.where("notice_id", "=", noticeId)) > 0;

        return result;
    }

    @Query(cmd = NOTICE_TEST1)
    public byte[] queryByteData(int noticeId) {
        LoginNoticeBean mm = Instance.sql().fetch(LoginNoticeBean.class, Cnd.where("notice_id", "=", noticeId)) ;
        TestMap ppp = ProtoUtil.deserializer(mm.getItem(), TestMap.class);
        return null;
    }
}
