package com.game.login;

import core.annotation.CtrlA;
import core.group.EventHandler;
import core.msg.TransferMsg;

public class LoginEventHandler implements EventHandler {
    @Override
    public void onEvent(TransferMsg transferMsg) {
        if (transferMsg.getHttpUrl().length() > 0) {
            CtrlA.getInstance().invokeHttpMethod(transferMsg);
        } else {
            CtrlA.getInstance().invokeMethod(transferMsg);
        }
    }
}
