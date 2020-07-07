package com.game.server.eventGroup.db;

import com.game.server.core.connect.ConnectionManager;
import com.game.server.core.groupHelper.EventHandler;
import com.game.server.core.msg.MsgBean;

/**
 * Created by Administrator on 2020/6/27.
 */
public class DBEventHandler implements EventHandler {
    @Override
    public void onEvent(MsgBean msgBean) {
        ConnectionManager.send2ClientByFD(msgBean.getId(), msgBean.packClientMsg());
    }
}
