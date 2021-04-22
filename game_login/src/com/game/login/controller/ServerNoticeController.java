package com.game.login.controller;

import bean.login.LoginPlayerInfoBean;
import bean.login.LoginNoticeBean;
import com.game.login.query.NoticeListQuery;
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
import protocal.remote.login.GetServerListReq;
import protocal.remote.login.ServerNoticeRsp;

import java.util.List;


@Ctrl
public class ServerNoticeController {

    private static Logger logger = LoggerFactory.getLogger(ServerNoticeController.class);

    @CtrlCmd(cmd = MsgConstant.MSG_NOTICE_LIST_REQ)
    public void getServerNotice(TransferMsg msg, ChannelHandlerContext context) {
        GetServerListReq serverListReq = ProtoUtil.deserializer(msg.getData(), GetServerListReq.class);
        String openId = serverListReq.getOpenId();
        LoginPlayerInfoBean playerBean = PlayerInfoQuery.queryPlayerInfo(openId);


        List<LoginNoticeBean> noticeList = NoticeListQuery.getAllNotice();
        ServerNoticeRsp serverNoticeRsp = new ServerNoticeRsp();
        serverNoticeRsp.setNoticeList(noticeList);
        HttpUtil.sendMsg(context, MsgConstant.MSG_NOTICE_LIST_RSP, serverNoticeRsp);
    }
}
