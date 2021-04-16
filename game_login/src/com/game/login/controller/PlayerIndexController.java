package com.game.login.controller;


import bean.login.LoginPlayerBean;
import com.game.login.query.PlayerInfoQuery;
import com.game.login.util.HttpUtil;
import core.annotation.Ctrl;
import core.annotation.CtrlCmd;
import core.msg.TransferMsg;
import core.util.ProtoUtil;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protocal.MsgConstant;
import protocal.remote.login.PlayerIndexReq;
import protocal.remote.login.PlayerIndexRsp;
import protocal.remote.system.ErroRsp;

import static core.Constants.SQL_RESULT_SUCCESS;
import static protocal.MsgConstant.MSG_GET_PLAYER_INDEX_RSP;

@Ctrl
public class PlayerIndexController {

    private static Logger logger = LoggerFactory.getLogger(ServerListController.class);

    @CtrlCmd(cmd = MsgConstant.MSG_GET_PLAYER_INDEX_REQ)
    public void getPlayerIndex(TransferMsg msg, ChannelHandlerContext context) {
        PlayerIndexReq playerIndexReq = ProtoUtil.deserializer(msg.getData(), PlayerIndexReq.class);
        String openId = playerIndexReq.getOpenId();
        boolean success = true;
        LoginPlayerBean playerBean = PlayerInfoQuery.queryPlayerInfo(openId);
        if (playerBean == null) {
            int result = PlayerInfoQuery.createPlayer(openId);
            if (result != SQL_RESULT_SUCCESS) {
                success = false;
            }
        }
        if (!success) {
            ErroRsp erroRsp = new ErroRsp();
            erroRsp.setErrorCode(1);
            HttpUtil.sendErrorMsg(context, MsgConstant.MSG_SERVER_LIST_RSP, erroRsp);
            return;
        }
        PlayerIndexRsp playerIndexRsp = new PlayerIndexRsp();
        playerIndexRsp.setPlayerIndex(playerBean.getPlayerIndex());
        HttpUtil.sendMsg(context, MSG_GET_PLAYER_INDEX_RSP, playerIndexRsp);
    }
}
