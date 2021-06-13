package com.game.login.controller;

import bean.login.LoginNoticeBean;
import com.game.login.util.HttpUtil;
import constants.MsgConstant;
import constants.TableKey;
import core.annotation.ctrl.Ctrl;
import core.annotation.ctrl.CtrlCmd;
import core.msg.TransferMsg;
import core.util.Ins;
import protocal.remote.login.ServerNoticeRsp;

import java.util.List;

import static constants.MsgConstant.HTTP_URL_GET_NOTICE;


@Ctrl
public class ServerNoticeController {

    @CtrlCmd(cmd = MsgConstant.MSG_NOTICE_LIST_REQ, httpCmd = HTTP_URL_GET_NOTICE)
    public void getServerNotice(TransferMsg msg) {
        List<LoginNoticeBean> noticeList = Ins.redis().fetchAll(TableKey.GAME_NOTICE_LIST);
        ServerNoticeRsp serverNoticeRsp = new ServerNoticeRsp();
        serverNoticeRsp.setNoticeList(noticeList);
        HttpUtil.sendMsg(msg, constants.MsgConstant.MSG_NOTICE_LIST_RSP, serverNoticeRsp);
    }
}
