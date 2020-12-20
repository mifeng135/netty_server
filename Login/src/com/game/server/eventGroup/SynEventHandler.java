package com.game.server.eventGroup;


import core.annotation.CtrlAnnotation;
import core.groupHelper.EventHandler;
import core.proto.TransferMsg;

/**
 * Created by Administrator on 2020/7/29.
 */
public class SynEventHandler implements EventHandler {

    @Override
    public void onEvent(TransferMsg transferMsg) {
        CtrlAnnotation.getInstance().invokeMethod(transferMsg.getMsgId(), transferMsg);
    }
}
