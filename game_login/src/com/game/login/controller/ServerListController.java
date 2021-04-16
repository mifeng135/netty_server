package com.game.login.controller;

import bean.login.LoginPlayerBean;
import bean.login.ServerInfoBean;
import com.game.login.query.PlayerInfoQuery;
import com.game.login.query.ServerListQuery;
import com.game.login.util.HttpUtil;
import core.annotation.Ctrl;
import core.annotation.CtrlCmd;
import core.msg.TransferMsg;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protocal.MsgConstant;
import protocal.remote.login.GetServerListRsp;

import java.util.List;


@Ctrl
public class ServerListController {

    private static Logger logger = LoggerFactory.getLogger(ServerListController.class);

    @CtrlCmd(cmd = MsgConstant.MSG_SERVER_LIST_REQ)
    public void getServerList(TransferMsg msg, ChannelHandlerContext context) {
        List<ServerInfoBean> serverList = ServerListQuery.queryAllServerList();
        GetServerListRsp serverListRsp = new GetServerListRsp();
        serverListRsp.setServerList(serverList);
        HttpUtil.sendMsg(context, MsgConstant.MSG_SERVER_LIST_RSP, serverListRsp);
    }
}
