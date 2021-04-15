package com.game.server.controller;


import bean.player.PlayerBean;
import bean.player.PlayerItemBean;
import bean.player.PlayerRoleBean;
import bean.player.PlayerSceneBean;
import com.game.server.query.PlayerInfoQuery;
import com.game.server.query.PlayerItemQuery;
import com.game.server.query.PlayerRoleQuery;
import com.game.server.query.PlayerSceneQuery;
import com.game.server.util.HttpUtil;
import core.annotation.Ctrl;
import core.annotation.CtrlCmd;
import core.msg.TransferMsg;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

import static protocal.MsgConstant.DB_CMD_QUERY_PLAYER_INFO;

@Ctrl
public class DBPlayerInfoController {

    @CtrlCmd(cmd = DB_CMD_QUERY_PLAYER_INFO)
    public void getPlayerInfo(TransferMsg msg, ChannelHandlerContext context) {
        int playerIndex = msg.getHeaderProto().getPlayerIndex();
        PlayerBean playerBean = PlayerInfoQuery.queryPlayer(playerIndex);
        if (playerBean == null) {
            msg.getHeaderProto().setSuccess(false);
            HttpUtil.sendMsg(context, msg.getHeaderProto(), new Object());
            return;
        }
        PlayerSceneBean playerSceneBean = PlayerSceneQuery.queryScene(playerIndex);
        List<PlayerItemBean> playerItemBean = PlayerItemQuery.queryPlayerItem(playerIndex);
        PlayerRoleBean playerRoleBean = PlayerRoleQuery.queryPlayerRole(playerIndex);
    }
}
