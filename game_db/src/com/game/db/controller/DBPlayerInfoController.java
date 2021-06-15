package com.game.db.controller;


import bean.player.PlayerInfoBean;
import bean.player.PlayerItemBean;
import bean.player.PlayerRoleBean;
import bean.player.PlayerSceneBean;
import com.game.db.util.MsgUtil;
import constants.TableKey;
import core.annotation.ctrl.Ctrl;
import core.annotation.ctrl.CtrlCmd;
import core.msg.TransferMsg;
import core.util.Ins;
import protocal.local.db.player.PlayerAllInfoDB;

import java.util.List;

import static constants.MsgConstant.*;


@Ctrl
public class DBPlayerInfoController {

    @CtrlCmd(cmd = DB_CMD_QUERY_ALL_PLAYER_INFO_REQ)
    public void getPlayerInfo(TransferMsg msg) {
        msg.getHeaderProto().setMsgId(DB_CMD_QUERY_ALL_PLAYER_INFO_RSP);
        int playerIndex = msg.getHeaderProto().getPlayerIndex();
        PlayerInfoBean playerBean = Ins.redis().fetch(TableKey.GAME_PLAYER, playerIndex);
        if (playerBean == null) {
            PlayerAllInfoDB playerAllInfoDB = new PlayerAllInfoDB();
            MsgUtil.sendMsg(msg, playerAllInfoDB);
            return;
        }
        PlayerSceneBean playerSceneBean = Ins.redis().fetch(TableKey.GAME_PLAYER_SCENE, playerIndex);
        List<PlayerItemBean> playerItemList = Ins.redis().fetch(TableKey.GAME_PLAYER_ITEM, playerIndex);
        PlayerRoleBean playerRoleBean = Ins.redis().fetch(TableKey.GAME_PLAYER_ROLE, playerIndex);

        PlayerAllInfoDB playerAllInfoDB = new PlayerAllInfoDB();
        playerAllInfoDB.setPlayerInfo(playerBean);
        playerAllInfoDB.setPlayerItemList(playerItemList);
        playerAllInfoDB.setPlayerScene(playerSceneBean);
        playerAllInfoDB.setPlayerRole(playerRoleBean);
        MsgUtil.sendMsg(msg, playerAllInfoDB);
    }
}
