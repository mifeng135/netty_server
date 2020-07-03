package com.game.server.eventGroup.game;

import com.game.server.core.connect.ConnectionManager;
import com.game.server.core.groupHelper.EventHandler;
import com.game.server.core.msg.MsgBean;

/**
 * Created by Administrator on 2020/7/3.
 */
public class GameEventHandler implements EventHandler {

    @Override
    public void onEvent(MsgBean msgBean) {
        ConnectionManager.send2ClientByFD(msgBean.getId(), msgBean.packClienMsg());
    }
}
