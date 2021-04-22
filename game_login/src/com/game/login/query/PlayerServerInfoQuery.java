package com.game.login.query;

import bean.login.LoginPlayerServerInfoBean;
import core.sql.SqlDao;
import org.nutz.dao.Cnd;

import java.util.List;

import static core.Constants.SQL_MASTER;

public class PlayerServerInfoQuery {

    public List<LoginPlayerServerInfoBean> queryPlayerServerInfo(int playerIndex) {
        return SqlDao.getInstance().getDao(SQL_MASTER).query(
                LoginPlayerServerInfoBean.class, Cnd.where("player_index", "=", playerIndex));
    }
}
