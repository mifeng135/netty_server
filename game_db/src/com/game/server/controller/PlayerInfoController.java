package com.game.server.controller;


import com.game.server.bean.PlayerBean;
import com.game.server.constant.RedisConstant;
import com.game.server.constant.SqlCmdConstant;
import com.game.server.util.TcpUtil;
import core.Constants;
import core.annotation.Ctrl;
import core.annotation.CtrlCmd;
import core.annotation.SqlAnnotation;
import core.manager.HttpConnectManager;
import core.msg.TransferMsg;
import core.redis.RedisManager;
import core.sql.MysqlBatchHandle;
import core.sql.MysqlBean;
import core.util.ProtoUtil;
import io.netty.channel.ChannelHandlerContext;
import org.redisson.api.RMap;
import protocol.db.PlayerInfoLoginReq;
import protocol.db.PlayerInfoLoginRsp;

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
            playerInfoLoginRsp.setReqPlayerIndex(playerInfoLoginReq.getReqPlayerIndex());
            playerInfoLoginRsp.setResult(MSG_RESULT_FAIL);
            TcpUtil.sendToLogin(msg.getPlayerIndex(), MSG_DB_PLAYER_INFO_LOGIN_RSP, playerInfoLoginRsp);
            return;
        }

        if (loginSuc) {
            PlayerInfoLoginRsp playerInfoLoginRsp = new PlayerInfoLoginRsp();
            playerInfoLoginRsp.setQueryMsgId(playerInfoLoginReq.getQueryMsgId());
            playerInfoLoginRsp.setQueryPlayerIndex(playerInfoLoginReq.getQueryPlayerIndex());
            playerInfoLoginRsp.setReqPlayerIndex(playerInfoLoginReq.getReqPlayerIndex());
            playerInfoLoginRsp.setName(playerBean.getName());
            playerInfoLoginRsp.setPlayerIndex(playerBean.getId());
            playerInfoLoginRsp.setResult(MSG_RESULT_SUCCESS);
            TcpUtil.sendToLogin(msg.getPlayerIndex(), MSG_DB_PLAYER_INFO_LOGIN_RSP, playerInfoLoginRsp);
        }

//        if (loginSuc) {
//            Channel channel = HttpConnectManager.getConnect(msgBean.getPlayerIndex());
//            String loginIp = channel.attr(Constants.REMOTE_ADDRESS).get();
//            int loginTime = TimeUtil.getCurrentTimeSecond();
//
//            playerBean.setLastLoginTime(loginTime);
//            playerBean.setLoginIp(loginIp);
//            loginMap.fastPut(playerBean.getAccount(), playerBean);
//        }
//
//        MysqlBean sqlBean = new MysqlBean();
//        sqlBean.setCmd(SqlCmdConstant.PLAYER_UPDATE_LOGIN_INFO);
//        sqlBean.setData(playerBean);
//        MysqlBatchHandle.getInstance().pushMsg(sqlBean);

//        if (!loginSuc) {
//            ErroRsp erroRsp = new ErroRsp();
//            erroRsp.setErrorStr("");
//            erroRsp.setMsgId(MsgConstant.MSG_LOGIN_RSP);
//            HttpUtil.sendErrorMsg(msgBean.getPlayerIndex(), MsgConstant.MSG_LOGIN_RSP, erroRsp);
//            return;
//        }
//        LoginRsp loginRsp = new LoginRsp();
//        loginRsp.setIp("127.0.0.1:7005");
//        loginRsp.setPlayerIndex(playerBean.getId());
//        loginRsp.setName(playerBean.getName());
//        HttpUtil.sendMsg(msgBean.getPlayerIndex(), MsgConstant.MSG_LOGIN_RSP, loginRsp);
    }
}
