package com.game.db.controller;


import bean.player.PlayerBean;
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
import io.netty.channel.ChannelHandlerContext;
import protocal.remote.user.EnterGameRsp;

import java.util.List;

import static protocal.MsgConstant.DB_CMD_QUERY_PLAYER_INFO_REQ;

@Ctrl
public class DBPlayerInfoController {

    @CtrlCmd(cmd = DB_CMD_QUERY_PLAYER_INFO_REQ)
    public void getPlayerInfo(TransferMsg msg, ChannelHandlerContext context) {
        int playerIndex = msg.getHeaderProto().getPlayerIndex();
        PlayerBean playerBean = PlayerInfoQuery.queryPlayer(playerIndex);
        if (playerBean == null) {
            EnterGameRsp enterGameRsp = new EnterGameRsp();
            enterGameRsp.setHasRole(false);
            MsgUtil.sendMsg(context, msg.getHeaderProto(), new EnterGameRsp());
            return;
        }
        PlayerSceneBean playerSceneBean = PlayerSceneQuery.queryScene(playerIndex);
        List<PlayerItemBean> playerItemList = PlayerItemQuery.queryPlayerItem(playerIndex);
        PlayerRoleBean playerRoleBean = PlayerRoleQuery.queryPlayerRole(playerIndex);

        EnterGameRsp enterGameRsp = new EnterGameRsp();
        enterGameRsp.setHasRole(true);
        enterGameRsp.setPlayerItemList(playerItemList);
        enterGameRsp.setPlayerScene(playerSceneBean);
        enterGameRsp.setPlayerRole(playerRoleBean);
        MsgUtil.sendMsg(context, msg.getHeaderProto(), enterGameRsp);
    }
}
