package com.game.db.controller;


import bean.player.PlayerInfoBean;
import bean.player.PlayerItemBean;
import bean.player.PlayerRoleBean;
import bean.player.PlayerSceneBean;
import com.game.db.query.PlayerInfoQuery;
import com.game.db.query.PlayerItemQuery;
import com.game.db.query.PlayerRoleQuery;
import com.game.db.query.PlayerSceneQuery;
import com.game.db.util.MsgUtil;
import core.annotation.Ctrl;
import core.annotation.CtrlCmd;
import core.msg.TransferMsg;
import protocal.local.db.player.PlayerAllInfoDB;

import java.util.List;

import static protocal.MsgConstant.DB_CMD_QUERY_ALL_PLAYER_INFO_REQ;

@Ctrl
public class DBPlayerInfoController {

    @CtrlCmd(cmd = DB_CMD_QUERY_ALL_PLAYER_INFO_REQ)
    public void getPlayerInfo(TransferMsg msg) {
        int playerIndex = msg.getHeaderProto().getPlayerIndex();
        PlayerInfoBean playerBean = PlayerInfoQuery.queryPlayer(playerIndex);
        if (playerBean == null) {
            PlayerAllInfoDB playerAllInfoDB = new PlayerAllInfoDB();
            MsgUtil.sendToLogic(msg, playerAllInfoDB);
            return;
        }
        PlayerSceneBean playerSceneBean = PlayerSceneQuery.queryScene(playerIndex);
        List<PlayerItemBean> playerItemList = PlayerItemQuery.queryPlayerItem(playerIndex);
        PlayerRoleBean playerRoleBean = PlayerRoleQuery.queryPlayerRole(playerIndex);

        PlayerAllInfoDB playerAllInfoDB = new PlayerAllInfoDB();
        playerAllInfoDB.setPlayerInfo(playerBean);
        playerAllInfoDB.setPlayerItemList(playerItemList);
        playerAllInfoDB.setPlayerScene(playerSceneBean);
        playerAllInfoDB.setPlayerRole(playerRoleBean);
        MsgUtil.sendToLogic(msg, playerAllInfoDB);
    }
}
