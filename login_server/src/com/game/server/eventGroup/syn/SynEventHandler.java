package com.game.server.eventGroup.syn;

import com.game.server.core.annotation.CtrlAnnotation;
import com.game.server.core.groupHelper.EventHandler;
import com.game.server.core.msg.MsgBean;

/**
 * Created by Administrator on 2020/7/29.
 */
public class SynEventHandler implements EventHandler{

    @Override
    public void onEvent(MsgBean msgBean) {
        CtrlAnnotation.getInstance().invokeMethod(msgBean.getCmd(), msgBean.getData());
    }
}
