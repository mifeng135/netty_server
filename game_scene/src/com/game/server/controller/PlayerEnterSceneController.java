package com.game.server.controller;

import com.game.server.util.TcpUtil;
import core.annotation.Ctrl;
import core.annotation.CtrlCmd;
import core.msg.TransferMsg;
import core.netty.asyncHttp.AsyncHttp;
import core.util.ProtoUtil;
import io.netty.channel.ChannelHandlerContext;
import protocol.local.db.scene.DBSceneReq;
import protocol.local.db.scene.DBSceneRsp;
import protocol.local.gate.PlayerEnterSceneReq;
import protocol.local.gate.PlayerEnterSceneRsp;

import static config.Config.DB_HTTP_URL;
import static protocol.MsgConstant.*;

@Ctrl
public class PlayerEnterSceneController {

    @CtrlCmd(cmd = MSG_PLAYER_ENTER_SCENE_REQ)
    public void playerEnterScene(TransferMsg msg, ChannelHandlerContext context) {
        PlayerEnterSceneReq playerEnterSceneReq = ProtoUtil.deserializer(msg.getData(), PlayerEnterSceneReq.class);
        int playerIndex = playerEnterSceneReq.getPlayerIndex();

        DBSceneReq dbSceneReq = new DBSceneReq();
        dbSceneReq.setPlayerIndex(playerIndex);
        dbSceneReq.setQueryMsgId(MSG_SYC_POSITION_REQ);
        dbSceneReq.setQueryPlayerIndex(playerIndex);

        byte[] data = AsyncHttp.getInstance().postSync(DB_HTTP_URL, MSG_DB_QUERY_SCENE, dbSceneReq);
        DBSceneRsp dbSceneRsp = ProtoUtil.deserializer(data, DBSceneRsp.class);

        PlayerEnterSceneRsp playerEnterSceneRsp = new PlayerEnterSceneRsp();
        playerEnterSceneRsp.setPlayerIndex(playerIndex);
        playerEnterSceneRsp.setPlayerSceneProto(dbSceneRsp.getPlayerSceneProto());
        TcpUtil.sendToGate(MSG_PLAYER_ENTER_SCENE_RSP, playerEnterSceneRsp);
    }
}
