package com.game.login;

import core.annotation.CA;
import core.group.EventHandler;
import core.msg.TransferMsg;

public class LoginEventHandler implements EventHandler {
    @Override
    public void onEvent(TransferMsg transferMsg) {
        if (transferMsg.getHttpUrl().length() > 0) {
            CA.getInstance().invokeHttpMethod(transferMsg);
        } else {
            CA.getInstance().invokeMethod(transferMsg);
        }
    }
}
