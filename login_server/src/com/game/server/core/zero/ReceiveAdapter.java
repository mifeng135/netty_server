package com.game.server.core.zero;

import com.game.server.core.msg.MsgBean;

/**
 * Created by Administrator on 2020/7/28.
 */
public interface ReceiveAdapter {
    public int getSectionId(MsgBean msgBean);

    public String getRegionString(MsgBean msgBean);
}
