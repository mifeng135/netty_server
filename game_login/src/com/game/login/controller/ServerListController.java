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
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protocal.MsgConstant;
import protocal.remote.login.GetServerListReq;
import protocal.remote.login.GetServerListRsp;

import java.util.List;


/***
 * 获取服务器列表
 */
@Ctrl
public class ServerListController {

    private static Logger logger = LoggerFactory.getLogger(ServerListController.class);

    @CtrlCmd(cmd = MsgConstant.MSG_SERVER_LIST_REQ)
    public void getServerList(TransferMsg msg, ChannelHandlerContext context) {
        GetServerListReq serverListReq = ProtoUtil.deserializer(msg.getData(), GetServerListReq.class);
        String openId = serverListReq.getOpenId();
        List<LoginPlayerServerInfoBean> selfServerList = getSelfPlayerServerList(openId);
        List<ServerListInfoBean> serverList = ServerListQuery.queryAllServerList();
        GetServerListRsp serverListRsp = new GetServerListRsp();
        serverListRsp.setServerList(serverList);
        serverListRsp.setSelfServerList(selfServerList);
        HttpUtil.sendMsg(context, MsgConstant.MSG_SERVER_LIST_RSP, serverListRsp);
    }

    private List<LoginPlayerServerInfoBean> getSelfPlayerServerList(String openId) {
        LoginPlayerInfoBean playerBean = PlayerInfoQuery.queryPlayerInfo(openId);
        if (playerBean != null) {
            return PlayerServerInfoQuery.queryPlayerServerInfo(playerBean.getPlayerIndex());
        } else {
            PlayerInfoQuery.createPlayer(openId);
        }
        return null;
    }
}
