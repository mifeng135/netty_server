package com.game.server.controller;


import com.game.server.bean.PlayerBean;
import com.game.server.constant.RedisConstant;
import com.game.server.constant.SqlCmdConstant;
import com.game.server.util.TcpUtil;
import core.annotation.Ctrl;
import core.annotation.CtrlCmd;
import core.annotation.SqlAnnotation;
import core.msg.TransferMsg;
import core.redis.RedisManager;
import core.util.ProtoUtil;
import io.netty.channel.ChannelHandlerContext;
import org.redisson.api.RMap;
import protocol.local.db.PlayerInfoLoginReq;
import protocol.local.db.PlayerInfoLoginRsp;

import static core.Constants.MSG_RESULT_FAIL;
import static core.Constants.MSG_RESULT_SUCCESS;
import static protocol.MsgConstant.*;

@Ctrl
public class PlayerInfoController {

    @CtrlCmd(cmd = MSG_DB_PLAYER_INFO_LOGIN_REQ)
    public void getPlayerInfoWithAccountAndPwd(TransferMsg msg, ChannelHandlerContext context) {

        PlayerInfoLoginReq playerInfoLoginReq = ProtoUtil.deserializer(msg.getData(), PlayerInfoLoginReq.class);
        String account = playerInfoLoginReq.getAccount();
        String password = playerInfoLoginReq.getPwd();


        RMap<String, PlayerBean> loginMap = RedisManager.getInstance().getRedisSon().getMap(RedisConstant.REDIS_LOGIN_KEY);
        PlayerBean playerBean = loginMap.get(account);

        boolean loginSuc = false;
        if (playerBean == null) {
            playerBean = new PlayerBean();
            playerBean.setAccount(account);
            playerBean.setPassword(password);
            PlayerBean result = SqlAnnotation.getInstance().sqlSelectOne(SqlCmdConstant.PLAYER_SELECT_ACCOUNT_PASSWORD, playerBean);
            if (result != null) {
                playerBean = result;
                loginSuc = true;
            }
        } else if (playerBean.getPassword().equals(password)) {
            loginSuc = true;
        }

        if (!loginSuc) {
            PlayerInfoLoginRsp playerInfoLoginRsp = new PlayerInfoLoginRsp();
            playerInfoLoginRsp.setQueryMsgId(playerInfoLoginReq.getQueryMsgId());
            playerInfoLoginRsp.setQueryPlayerIndex(playerInfoLoginReq.getQueryPlayerIndex());
            playerInfoLoginRsp.setPlayerIndex(playerInfoLoginReq.getPlayerIndex());
            playerInfoLoginRsp.setResult(MSG_RESULT_FAIL);
            TcpUtil.sendToLogin(MSG_DB_PLAYER_INFO_LOGIN_RSP, playerInfoLoginRsp);
            return;
        }

        if (loginSuc) {
            PlayerInfoLoginRsp playerInfoLoginRsp = new PlayerInfoLoginRsp();
            playerInfoLoginRsp.setQueryMsgId(playerInfoLoginReq.getQueryMsgId());
            playerInfoLoginRsp.setQueryPlayerIndex(playerInfoLoginReq.getQueryPlayerIndex());
            playerInfoLoginRsp.setPlayerIndex(playerInfoLoginReq.getPlayerIndex());
            playerInfoLoginRsp.setName(playerBean.getName());
            playerInfoLoginRsp.setId(playerBean.getId());
            playerInfoLoginRsp.setResult(MSG_RESULT_SUCCESS);
            TcpUtil.sendToLogin(MSG_DB_PLAYER_INFO_LOGIN_RSP, playerInfoLoginRsp);
        }
    }
}
