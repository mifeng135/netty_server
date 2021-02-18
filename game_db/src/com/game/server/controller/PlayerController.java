package com.game.server.controller;


import com.game.server.bean.PlayerBean;
import com.game.server.constant.RedisConstant;
import com.game.server.constant.SqlCmdConstant;
import com.game.server.util.HttpUtil;
import core.annotation.Ctrl;
import core.annotation.CtrlCmd;
import core.annotation.SqlAnnotation;
import core.msg.TransferMsg;
import core.redis.RedisManager;
import core.util.ProtoUtil;
import io.netty.channel.ChannelHandlerContext;
import org.redisson.api.RMap;
import protocol.local.db.login.DBLoginReq;
import protocol.local.db.login.DBLoginRsp;

import static core.Constants.MSG_RESULT_FAIL;
import static core.Constants.MSG_RESULT_SUCCESS;
import static protocol.MsgConstant.*;

@Ctrl
public class PlayerController {

    @CtrlCmd(cmd = MSG_DB_QUERY_LOGIN)
    public void queryPlayerInfo(TransferMsg msg, ChannelHandlerContext context) {
        DBLoginReq playerInfoLoginReq = ProtoUtil.deserializer(msg.getData(), DBLoginReq.class);
        String account = playerInfoLoginReq.getAccount();
        String password = playerInfoLoginReq.getPwd();

        PlayerBean playerBean = new PlayerBean();
        playerBean.setAccount(account);
        playerBean.setPassword(password);
        PlayerBean result = SqlAnnotation.getInstance().sqlSelectOne(SqlCmdConstant.PLAYER_SELECT_ACCOUNT_PASSWORD, playerBean);

        if (result != null) {
            RMap<String, PlayerBean> loginMap = RedisManager.getInstance().getRedisSon().getMap(RedisConstant.REDIS_LOGIN_KEY);
            loginMap.fastPut(result.getAccount(), result);
            DBLoginRsp dbLoginRsp = new DBLoginRsp();
            dbLoginRsp.setQueryMsgId(playerInfoLoginReq.getQueryMsgId());
            dbLoginRsp.setQueryPlayerIndex(playerInfoLoginReq.getQueryPlayerIndex());
            dbLoginRsp.setPlayerIndex(playerInfoLoginReq.getPlayerIndex());
            dbLoginRsp.setName(playerBean.getName());
            dbLoginRsp.setId(playerBean.getId());
            dbLoginRsp.setResult(MSG_RESULT_SUCCESS);
            HttpUtil.sendMsg(context, dbLoginRsp);
            return;
        }
        DBLoginRsp dbLoginRsp = new DBLoginRsp();
        dbLoginRsp.setResult(MSG_RESULT_FAIL);
        dbLoginRsp.setPlayerIndex(playerInfoLoginReq.getPlayerIndex());
        HttpUtil.sendMsg(context, dbLoginRsp);
    }
}
