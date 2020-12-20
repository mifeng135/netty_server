package com.game.server.eventGroup;


import core.annotation.CtrlAnnotation;
import core.groupHelper.EventHandler;
import core.proto.TransferMsg;

/**
 * Created by Administrator on 2020/6/23.
 */
public class LoginEventHandler implements EventHandler {

    @Override
    public void onEvent(TransferMsg transferMsg) {
        CtrlAnnotation.getInstance().invokeMethod(transferMsg.getMsgId(), transferMsg);
    }
}
