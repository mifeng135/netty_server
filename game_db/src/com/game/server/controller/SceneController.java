package com.game.server.controller;


import bean.player.PlayerScene;
import com.game.server.query.SceneQuery;
import com.game.server.util.HttpUtil;
import core.annotation.Ctrl;
import core.annotation.CtrlCmd;
import core.msg.TransferMsg;
import io.netty.channel.ChannelHandlerContext;
import protocal.local.common.PlayerSceneProto;
import protocal.local.db.scene.DBSceneRsp;

import static protocal.MsgConstant.MSG_DB_QUERY_SCENE;

@Ctrl
public class SceneController {

    @CtrlCmd(cmd = MSG_DB_QUERY_SCENE)
    public void querySceneInfo(TransferMsg msg, ChannelHandlerContext context) {
        int playerIndex = msg.getHeaderProto().getPlayerIndex();
        PlayerScene playerScene = SceneQuery.queryScene(playerIndex);

        PlayerSceneProto playerSceneProto = new PlayerSceneProto();
        playerSceneProto.setSceneId(playerScene.getSceneId());
        playerSceneProto.setPosX(playerScene.getPlayerPositionX());
        playerSceneProto.setPosy(playerScene.getPlayerPositionY());

        DBSceneRsp dbSceneRsp = new DBSceneRsp();
        dbSceneRsp.setPlayerSceneProto(playerSceneProto);
        HttpUtil.sendMsg(context, msg.getHeaderProto(), dbSceneRsp);
    }
}
