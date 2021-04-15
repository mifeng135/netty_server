package com.game.server.controller;

import bean.login.LoginPlayerBean;
import bean.login.ServerInfoBean;
import com.game.server.query.PlayerInfoQuery;
import com.game.server.query.ServerListQuery;
import com.game.server.util.HttpUtil;
import core.annotation.Ctrl;
import core.annotation.CtrlCmd;
import core.msg.TransferMsg;
import core.util.ProtoUtil;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protocal.MsgConstant;
import protocal.remote.login.GetServerListReq;
import protocal.remote.login.GetServerListRsp;
import protocal.remote.system.ErroRsp;

import java.util.List;

import static core.Constants.SQL_RESULT_FAIL;
import static core.Constants.SQL_RESULT_SUCCESS;

@Ctrl
public class ServerListController {

    private static Logger logger = LoggerFactory.getLogger(ServerListController.class);

    @CtrlCmd(cmd = MsgConstant.MSG_SERVER_LIST_REQ)
    public void getServerList(TransferMsg msg, ChannelHandlerContext context) {
        GetServerListReq getServerListReq = ProtoUtil.deserializer(msg.getData(), GetServerListReq.class);
        String openId = getServerListReq.getOpenId();
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
        List<ServerInfoBean> serverList = ServerListQuery.queryAllServerList();
        GetServerListRsp serverListRsp = new GetServerListRsp();
        serverListRsp.setServerList(serverList);
        serverListRsp.setPlayerIndex(playerBean.getPlayerIndex());
        HttpUtil.sendMsg(context, MsgConstant.MSG_SERVER_LIST_RSP, serverListRsp);
    }
}
