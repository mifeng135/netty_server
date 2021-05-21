package com.game.login.controller;

import bean.login.LoginPlayerInfoBean;
import bean.login.LoginPlayerServerInfoBean;
import bean.login.ServerListInfoBean;
import com.game.login.query.PlayerInfoQuery;
import com.game.login.query.PlayerServerInfoQuery;
import com.game.login.query.ServerListQuery;
import com.game.login.util.HttpUtil;
import core.annotation.Ctrl;
import core.annotation.CtrlCmd;
import core.msg.TransferMsg;
import core.util.ProtoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protocal.MsgConstant;
import protocal.remote.login.GetServerListReq;
import protocal.remote.login.GetServerListRsp;

import java.util.List;

import static protocal.MsgConstant.HTTP_URL_GET_SERVER_LIST;


/***
 * 获取服务器列表
 */
@Ctrl
public class ServerListController {

    private static Logger logger = LoggerFactory.getLogger(ServerListController.class);

    @CtrlCmd(cmd = MsgConstant.MSG_SERVER_LIST_REQ)
    public void getServerList(TransferMsg msg) {
        GetServerListReq serverListReq = ProtoUtil.deserializer(msg.getData(), GetServerListReq.class);
        String openId = serverListReq.getOpenId();
        process(openId, msg);
    }

    @CtrlCmd(httpCmd = HTTP_URL_GET_SERVER_LIST)
    public void httpGetServerList(TransferMsg msg) {
        String openId = msg.getParams().get("openId");
        process(openId, msg);
    }

    private void process(String openId, TransferMsg msg) {
        LoginPlayerInfoBean playerInfoBean = queryLoginPlayerInfo(openId);
        List<LoginPlayerServerInfoBean> selfServerList = PlayerServerInfoQuery.queryPlayerServerInfo(playerInfoBean.getPlayerIndex());
        List<ServerListInfoBean> serverList = ServerListQuery.queryAllServerList();
        GetServerListRsp serverListRsp = new GetServerListRsp();
        serverListRsp.setServerList(serverList);
        serverListRsp.setSelfServerList(selfServerList);
        serverListRsp.setPlayerIndex(playerInfoBean.getPlayerIndex());
        HttpUtil.sendMsg(msg, MsgConstant.MSG_SERVER_LIST_RSP, serverListRsp);
    }

    private LoginPlayerInfoBean queryLoginPlayerInfo(String openId) {
        LoginPlayerInfoBean playerBean = PlayerInfoQuery.queryPlayerInfo(openId);
        if (playerBean == null) {
            playerBean = PlayerInfoQuery.createPlayer(openId);
        }
        return playerBean;
    }
}
