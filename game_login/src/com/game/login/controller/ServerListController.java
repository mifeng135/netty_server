package com.game.login.controller;

import bean.login.LoginPlayerInfoBean;
import bean.login.LoginPlayerServerInfoBean;
import bean.login.ServerListInfoBean;
import com.game.login.query.PlayerInfoQuery;
import com.game.login.query.PlayerServerInfoQuery;
import com.game.login.query.ServerListQuery;
import com.game.login.util.HttpJsonUtil;
import com.game.login.util.HttpUtil;
import core.Constants;
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
    public void getServerList(TransferMsg msg) {
        GetServerListReq serverListReq = ProtoUtil.deserializer(msg.getData(), GetServerListReq.class);
        String openId = serverListReq.getOpenId();
        process(openId, msg.getContext(), Constants.HTTP_DECODER_TYPE_PROTO_BUFFER);
    }

    @CtrlCmd(httpCmd = "/getServerList")
    public void httpGetServerList(TransferMsg msg) {
        String openId = msg.getParams().get("openId");
        process(openId, msg.getContext(), Constants.HTTP_DECODER_TYPE_JSON);
    }

    private void process(String openId, ChannelHandlerContext context, int type) {
        LoginPlayerInfoBean playerInfoBean = queryLoginPlayerInfo(openId);
        List<LoginPlayerServerInfoBean> selfServerList = PlayerServerInfoQuery.queryPlayerServerInfo(playerInfoBean.getPlayerIndex());
        List<ServerListInfoBean> serverList = ServerListQuery.queryAllServerList();
        GetServerListRsp serverListRsp = new GetServerListRsp();
        serverListRsp.setServerList(serverList);
        serverListRsp.setSelfServerList(selfServerList);
        serverListRsp.setPlayerIndex(playerInfoBean.getPlayerIndex());
        HttpJsonUtil.sendStringMsg(context, serverListRsp);
        //HttpUtil.sendMsg(context, MsgConstant.MSG_SERVER_LIST_RSP, serverListRsp);
    }

    private LoginPlayerInfoBean queryLoginPlayerInfo(String openId) {
        LoginPlayerInfoBean playerBean = PlayerInfoQuery.queryPlayerInfo(openId);
        if (playerBean == null) {
            playerBean = PlayerInfoQuery.createPlayer(openId);
        }
        return playerBean;
    }
}
