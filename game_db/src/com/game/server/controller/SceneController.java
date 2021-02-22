package com.game.server.controller;


import com.game.server.bean.PlayerScene;
import com.game.server.query.QueryPlayerInfo;
import com.game.server.redis.RedisCache;
import com.game.server.util.HttpUtil;
import core.annotation.Ctrl;
import core.annotation.CtrlCmd;
import core.annotation.SqlAnnotation;
import core.msg.TransferMsg;
import core.util.ProtoUtil;
import io.netty.channel.ChannelHandlerContext;
import org.redisson.api.RMapCache;
import protocol.local.common.PlayerSceneProto;
import protocol.local.db.scene.DBSceneReq;
import protocol.local.db.scene.DBSceneRsp;

import static protocol.MsgConstant.MSG_DB_QUERY_SCENE;

@Ctrl
public class SceneController {

    @CtrlCmd(cmd = MSG_DB_QUERY_SCENE)
    public void querySceneInfo(TransferMsg msg, ChannelHandlerContext context) {
        DBSceneReq dbSceneReq = ProtoUtil.deserializer(msg.getData(), DBSceneReq.class);
        int playerIndex = dbSceneReq.getPlayerIndex();

        PlayerScene playerScene = QueryPlayerInfo.queryScene(playerIndex);

        PlayerSceneProto playerSceneProto = new PlayerSceneProto();
        playerSceneProto.setSceneId(playerScene.getSceneId());
        playerSceneProto.setPosX(playerScene.getPlayerPositionX());
        playerSceneProto.setPosy(playerScene.getPlayerPositionY());

        DBSceneRsp dbSceneRsp = new DBSceneRsp();
        dbSceneRsp.setPlayerIndex(playerIndex);
        dbSceneRsp.setQueryMsgId(MSG_DB_QUERY_SCENE);
        dbSceneRsp.setQueryPlayerIndex(playerIndex);
        dbSceneRsp.setPlayerSceneProto(playerSceneProto);
        HttpUtil.sendMsg(context, dbSceneRsp);
    }
}
