package com.game.login.controller;

import constants.MsgConstant;
import core.annotation.Ctrl;
import core.annotation.CtrlCmd;
import core.msg.TransferMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static constants.MsgConstant.HTTP_URL_GET_NOTICE;


@Ctrl
public class ServerNoticeController {

    private static Logger logger = LoggerFactory.getLogger(ServerNoticeController.class);

    @CtrlCmd(cmd = MsgConstant.MSG_NOTICE_LIST_REQ, httpCmd = HTTP_URL_GET_NOTICE)
    public void getServerNotice(TransferMsg msg) {
//        List<LoginNoticeBean> noticeList = NoticeListQuery.getAllNotice();
//        ServerNoticeRsp serverNoticeRsp = new ServerNoticeRsp();
//        serverNoticeRsp.setNoticeList(noticeList);
//        HttpUtil.sendMsg(msg, constants.MsgConstant.MSG_NOTICE_LIST_RSP, serverNoticeRsp);
    }
}
