package com.game.db.eventGroup;

import com.game.db.core.annotation.CtrlAnnotation;
import com.game.db.core.groupHelper.EventHandler;
import com.game.db.core.msg.MsgBean;

/**
 * Created by Administrator on 2020/6/27.
 */
public class DBEventHandler implements EventHandler {
    @Override
    public void onEvent(MsgBean msgBean) {
        CtrlAnnotation.getInstance().invokeMethod(msgBean.getCmd(), msgBean.getId(), msgBean.getData());
    }
}
