package com.game.login.query;

import bean.player.PlayerServerInfoBean;
import core.annotation.SqlAnnotation;

import java.util.List;

import static com.game.login.constant.SqlCmdConstant.PLAYER_SERVER_INFO_SELECT;

public class PlayerServerInfoQuery {

    public List<PlayerServerInfoBean> queryPlayerServerInfo(int playerIndex) {
        return SqlAnnotation.getInstance().sqlSelectList(PLAYER_SERVER_INFO_SELECT, playerIndex);
    }
}
