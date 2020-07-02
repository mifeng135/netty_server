package com.game.login.eventGroup;


import com.game.login.core.annotation.CtrlAnnotation;
import com.game.login.core.groupHelper.EventHandler;
import com.game.login.core.msg.MsgBean;

/**
 * Created by Administrator on 2020/6/23.
 */
public class LoginEventHandler implements EventHandler {

    @Override
    public void onEvent(MsgBean msgBean) {
        CtrlAnnotation.getInstance().invokeMethod(msgBean.getCmd(), msgBean.getContext(), msgBean.getData());
    }
}
