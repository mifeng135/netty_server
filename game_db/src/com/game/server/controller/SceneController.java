package com.game.server.controller;


import com.game.server.bean.PlayerScene;
import com.game.server.util.HttpUtil;
import core.annotation.Ctrl;
import core.annotation.CtrlCmd;
import core.annotation.SqlAnnotation;
import core.msg.TransferMsg;
import core.util.ProtoUtil;
import io.netty.channel.ChannelHandlerContext;
import protocol.local.db.scene.DBSceneReq;
import protocol.local.db.scene.DBSceneRsp;

import static com.game.server.constant.SqlCmdConstant.*;
import static protocol.MsgConstant.MSG_DB_QUERY_SCENE;

@Ctrl
public class SceneController {

    @CtrlCmd(cmd = MSG_DB_QUERY_SCENE)
    public void querySceneInfo(TransferMsg msg, ChannelHandlerContext context) {
        DBSceneReq dbSceneReq = ProtoUtil.deserializer(msg.getData(), DBSceneReq.class);
        int playerIndex = dbSceneReq.getPlayerIndex();
        PlayerScene playerScene = SqlAnnotation.getInstance().sqlSelectOne(PLAYER_SCENE_SELECT_SCENE_INFO, playerIndex);

        DBSceneRsp dbSceneRsp = new DBSceneRsp();
        dbSceneRsp.setPlayerIndex(playerIndex);
        dbSceneRsp.setQueryMsgId(MSG_DB_QUERY_SCENE);
        dbSceneRsp.setQueryPlayerIndex(playerIndex);
        dbSceneRsp.setPositionX(playerScene.getPlayerPositionX());
        dbSceneRsp.setPositionY(playerScene.getPlayerPositionY());
        dbSceneRsp.setSceneId(playerScene.getSceneId());
        HttpUtil.sendMsg(context, dbSceneRsp);
    }
}
