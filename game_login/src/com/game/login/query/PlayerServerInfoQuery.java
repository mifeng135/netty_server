package com.game.login.query;

import bean.login.LoginPlayerServerInfoBean;
import com.game.login.redis.RedisCache;
import core.sql.SqlDao;
import org.nutz.dao.Cnd;
import org.redisson.api.RMap;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static core.Constants.SQL_MASTER;

public class PlayerServerInfoQuery {

    /**
     * 查询玩家所在服务器信息
     *
     * @param playerIndex
     * @return
     */
    public List<LoginPlayerServerInfoBean> queryPlayerServerInfo(int playerIndex) {
        RMap<Integer, List<LoginPlayerServerInfoBean>> redisCache = RedisCache.getInstance().getPlayerServerInfoCache();
        List<LoginPlayerServerInfoBean> serverInfoBeanList = redisCache.get(playerIndex);
        if (serverInfoBeanList == null) {
            serverInfoBeanList = SqlDao.getInstance().getDao(SQL_MASTER).query(LoginPlayerServerInfoBean.class,
                    Cnd.where("player_index", "=", playerIndex));

            Map<Integer, List<LoginPlayerServerInfoBean>> playerMapInfo = serverInfoBeanList.stream().
                    collect(Collectors.groupingBy(LoginPlayerServerInfoBean::getPlayerIndex));

            redisCache.putAll(playerMapInfo);
        }
        return serverInfoBeanList;
    }
}
