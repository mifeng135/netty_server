package com.game.server.eventGroup.room;

import com.game.server.core.annotation.CtrlAnnotation;
import com.game.server.core.groupHelper.EventHandler;
import com.game.server.core.msg.MsgBean;

/**
 * Created by Administrator on 2020/7/3.
 */
public class RoomEventHandler implements EventHandler {

    @Override
    public void onEvent(MsgBean msgBean) {
        CtrlAnnotation.getInstance().invokeMethod(msgBean.getCmd(),msgBean.getId(),msgBean.getData());
    }
}
