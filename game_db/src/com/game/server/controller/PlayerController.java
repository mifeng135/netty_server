package com.game.server.controller;


import com.game.server.bean.PlayerBean;
import com.game.server.query.QueryPlayerInfo;
import com.game.server.redis.RedisCache;
import com.game.server.util.HttpUtil;
import core.annotation.Ctrl;
import core.annotation.CtrlCmd;
import core.msg.TransferMsg;
import core.util.ProtoUtil;
import io.netty.channel.ChannelHandlerContext;
import protocal.local.db.login.DBLoginReq;
import protocal.local.db.login.DBLoginRsp;

import static protocal.MsgConstant.*;

@Ctrl
public class PlayerController {

    @CtrlCmd(cmd = MSG_DB_QUERY_LOGIN)
    public void queryPlayerInfo(TransferMsg msg, ChannelHandlerContext context) {
        DBLoginReq dbLoginReq = ProtoUtil.deserializer(msg.getData(), DBLoginReq.class);
        String account = dbLoginReq.getAccount();
        String password = dbLoginReq.getPwd();

        PlayerBean playerBean = QueryPlayerInfo.queryPlayerInfo(account, password);
        if (playerBean == null) {
            DBLoginRsp dbLoginRsp = new DBLoginRsp();
            HttpUtil.sendMsg(context, msg.getHeaderProto(), dbLoginRsp);
            return;
        }

        RedisCache.getInstance().getAccountLoginCache().fastPut(playerBean.getAccount(), playerBean);
        DBLoginRsp dbLoginRsp = new DBLoginRsp();
        dbLoginRsp.setName(playerBean.getName());
        dbLoginRsp.setId(playerBean.getId());
        HttpUtil.sendMsg(context, msg.getHeaderProto(), dbLoginRsp);
    }
}
