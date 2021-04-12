package com.game.server.controller;

import com.game.server.bean.ServerListBean;
import com.game.server.query.ServerListQuery;
import com.game.server.util.HttpUtil;
import core.annotation.Ctrl;
import core.annotation.CtrlCmd;
import core.msg.TransferMsg;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protocal.MsgConstant;
import protocal.remote.login.GetServerListRsp;
import protocal.remote.login.ServerInfo;

import java.util.ArrayList;
import java.util.List;

@Ctrl
public class ServerListController {

    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @CtrlCmd(cmd = MsgConstant.MSG_GET_SERVER_LIST_REQ)
    public void getServerList(TransferMsg msg, ChannelHandlerContext context) {
        List<ServerListBean> serverList = ServerListQuery.queryAllServerList();
        GetServerListRsp serverListRsp = new GetServerListRsp();
        List<ServerInfo> serverListArray = new ArrayList<>();
        for (int i = 0; i < serverList.size(); i++) {
            ServerListBean serverListBean = serverList.get(i);
            ServerInfo serverInfo = new ServerInfo();
            serverInfo.setOpenTime(serverListBean.getOpenTime());
            serverInfo.setServerId(serverListBean.getServerId());
            serverInfo.setServerIp(serverListBean.getServerIp());
            serverInfo.setServerName(serverListBean.getServerName());
            serverInfo.setState(serverListBean.getState());
            serverListArray.add(serverInfo);
        }
        serverListRsp.setServerList(serverListArray);
        HttpUtil.sendMsg(msg.getHeaderProto().getPlayerIndex(), MsgConstant.MSG_GET_SERVER_LIST_RSP, serverListRsp);
    }
}
