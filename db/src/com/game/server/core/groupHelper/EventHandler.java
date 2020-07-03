package com.game.server.core.groupHelper;


import com.game.server.core.msg.MsgBean;

/**
 * Created by Administrator on 2020/6/19.
 */
public interface EventHandler {

    public void onEvent(MsgBean msgBean);
}
