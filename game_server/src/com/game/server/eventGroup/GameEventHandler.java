package com.game.server.eventGroup;

import com.game.server.core.annotation.CtrlAnnotation;
import com.game.server.core.groupHelper.EventHandler;
import com.game.server.core.msg.MsgBean;

/**
 * Created by Administrator on 2020/7/3.
 */
public class GameEventHandler implements EventHandler {

    @Override
    public void onEvent(MsgBean msgBean) {
        CtrlAnnotation.getInstance().invokeMethod(msgBean.getCmd(), msgBean);
    }
}
