package com.game.server.eventGroup.room;

import com.game.server.constant.MsgRegionConstant;
import com.game.server.core.groupHelper.EventHandler;
import com.game.server.core.groupHelper.EventThreadGroup;

/**
 * process create room join room link broke
 * Created by Administrator on 2020/7/3.
 */
public class RoomEventGroup extends EventThreadGroup {

    public RoomEventGroup(Class<? extends EventHandler> handler, int count) {
        super(handler, count);
    }

    @Override
    protected String getRegionName() {
        return MsgRegionConstant.MSG_REGION_ROOM;
    }
}
