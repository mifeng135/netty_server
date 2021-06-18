package com.game.login.controller;

import core.annotation.ctrl.Ctrl;
import core.annotation.ctrl.CtrlCmd;
import core.msg.ClientExceptionMsg;
import core.msg.TransferMsg;
import core.util.ProtoUtil;
import core.util.SaveClientErrorUtil;

import static constants.MsgConstant.MSG_CLIENT_ERROR_REPORT;


@Ctrl
public class ClientErrorController {

    @CtrlCmd(cmd = MSG_CLIENT_ERROR_REPORT)
    public void getServerList(TransferMsg msg) {
        ClientExceptionMsg clientExceptionMsg = ProtoUtil.deserializer(msg.getData(), ClientExceptionMsg.class);
        clientExceptionMsg.setTime(System.currentTimeMillis());
        SaveClientErrorUtil.getInstance().pushErrorMsg(clientExceptionMsg);
    }
}
