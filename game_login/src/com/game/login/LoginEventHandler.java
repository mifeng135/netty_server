package com.game.login;

import core.annotation.ctrl.CtrlA;
import core.group.EventHandler;
import core.msg.TransferMsg;

import static core.Constants.HTTP_DECODER_TYPE_JSON;

public class LoginEventHandler implements EventHandler {
    @Override
    public void onEvent(TransferMsg transferMsg) {
        if (transferMsg.getMsgType() == HTTP_DECODER_TYPE_JSON) {
            CtrlA.getInstance().invokeHttpMethod(transferMsg);
        } else {
            CtrlA.getInstance().invokeMethod(transferMsg);
        }
    }
}
