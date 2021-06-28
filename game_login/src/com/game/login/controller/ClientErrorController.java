package com.game.login.controller;

import core.annotation.ctrl.Ctrl;
import core.annotation.ctrl.CtrlCmd;
import core.msg.ClientExceptionPush;
import core.msg.TransferMsg;
import core.util.ProtoUtil;
import core.util.SaveClientErrorUtil;

import static constants.MsgConstant.MSG_CLIENT_ERROR_REPORT_PUSH;


@Ctrl
public class ClientErrorController {

    @CtrlCmd(cmd = MSG_CLIENT_ERROR_REPORT_PUSH)
    public void getServerList(TransferMsg msg) {
        ClientExceptionPush clientExceptionMsg = ProtoUtil.deserializer(msg.getData(), ClientExceptionPush.class);
        clientExceptionMsg.setTime(System.currentTimeMillis());
        SaveClientErrorUtil.getInstance().pushErrorMsg(clientExceptionMsg);
    }
}
