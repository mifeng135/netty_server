package com.game.server.controller;


import bean.player.PlayerSceneBean;
import com.game.server.query.PlayerSceneQuery;
import com.game.server.util.HttpUtil;
import core.annotation.Ctrl;
import core.annotation.CtrlCmd;
import core.msg.TransferMsg;
import io.netty.channel.ChannelHandlerContext;
import protocal.local.db.scene.DBSceneRsp;

import static protocal.MsgConstant.MSG_DB_QUERY_SCENE;

@Ctrl
public class SceneController {

    @CtrlCmd(cmd = MSG_DB_QUERY_SCENE)
    public void querySceneInfo(TransferMsg msg, ChannelHandlerContext context) {
        int playerIndex = msg.getHeaderProto().getPlayerIndex();
        PlayerSceneBean playerScene = PlayerSceneQuery.queryScene(playerIndex);
        DBSceneRsp dbSceneRsp = new DBSceneRsp();
        dbSceneRsp.setPlayerScene(playerScene);
        HttpUtil.sendMsg(context, msg.getHeaderProto(), dbSceneRsp);
    }
}
