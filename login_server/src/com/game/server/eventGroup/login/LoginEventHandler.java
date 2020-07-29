package com.game.server.eventGroup.login;


import com.game.server.core.annotation.CtrlAnnotation;
import com.game.server.core.groupHelper.EventHandler;
import com.game.server.core.msg.MsgBean;

/**
 * Created by Administrator on 2020/6/23.
 */
public class LoginEventHandler implements EventHandler {

    @Override
    public void onEvent(MsgBean msgBean) {
        CtrlAnnotation.getInstance().invokeMethod(msgBean.getCmd(), msgBean.getContext(), msgBean.getData());
    }
}