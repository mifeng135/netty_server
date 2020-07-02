package com.game.gate.eventGroup.db;

import com.game.gate.core.connect.ConnectionManager;
import com.game.gate.core.groupHelper.EventHandler;
import com.game.gate.core.msg.MsgBean;

/**
 * Created by Administrator on 2020/6/27.
 */
public class DBEventHandler implements EventHandler {
    @Override
    public void onEvent(MsgBean msgBean) {
        ConnectionManager.send2ClientByFD(msgBean.getId(), msgBean.packClienMsg());
    }
}
