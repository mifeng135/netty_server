package com.game.login;

import core.annotation.CtrlAnnotation;
import core.group.EventHandler;
import core.msg.TransferMsg;

public class LoginEventHandler implements EventHandler {
    @Override
    public void onEvent(TransferMsg transferMsg) {
        if (transferMsg.getHttpUrl().length() > 0) {
            CtrlAnnotation.getInstance().invokeHttpMethod(transferMsg);
        } else {
            CtrlAnnotation.getInstance().invokeMethod(transferMsg);
        }
    }
}
