package com.game.db.controller;


import bean.player.PlayerSceneBean;
import com.game.db.query.PlayerSceneQuery;
import com.game.db.util.HttpUtil;
import core.annotation.Ctrl;
import core.annotation.CtrlCmd;
import core.msg.TransferMsg;
import io.netty.channel.ChannelHandlerContext;
import protocal.local.db.scene.DBSceneRsp;

import static protocal.MsgConstant.*;

@Ctrl
public class DBSceneController {

    @CtrlCmd(cmd = DB_CMD_QUERY_SCENE)
    public void querySceneInfo(TransferMsg msg, ChannelHandlerContext context) {
        int playerIndex = msg.getHeaderProto().getPlayerIndex();
        PlayerSceneBean playerScene = PlayerSceneQuery.queryScene(playerIndex);
        DBSceneRsp dbSceneRsp = new DBSceneRsp();
        dbSceneRsp.setPlayerScene(playerScene);
        HttpUtil.sendMsg(context, msg.getHeaderProto(), dbSceneRsp);
    }
}
