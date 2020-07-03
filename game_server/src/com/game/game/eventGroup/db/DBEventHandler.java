package com.game.game.eventGroup.db;

import com.game.game.core.connect.ConnectionManager;
import com.game.game.core.groupHelper.EventHandler;
import com.game.game.core.msg.MsgBean;

/**
 * Created by Administrator on 2020/6/27.
 */
public class DBEventHandler implements EventHandler {
    @Override
    public void onEvent(MsgBean msgBean) {
        ConnectionManager.send2ClientById(msgBean.getId(), msgBean.packClientMsg());
    }
}
