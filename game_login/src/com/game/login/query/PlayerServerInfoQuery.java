package com.game.login.query;

import bean.player.PlayerServerInfoBean;
import core.annotation.SqlAnnotation;
import core.sql.SqlDao;
import org.nutz.dao.Cnd;

import java.util.List;

import static com.game.login.constant.SqlCmdConstant.PLAYER_SERVER_INFO_SELECT;
import static core.Constants.SQL_MASTER;

public class PlayerServerInfoQuery {

    public List<PlayerServerInfoBean> queryPlayerServerInfo(int playerIndex) {
        return SqlDao.getInstance().getDao(SQL_MASTER).query(
                PlayerServerInfoBean.class, Cnd.where("player_index", "=", playerIndex));
    }
}
