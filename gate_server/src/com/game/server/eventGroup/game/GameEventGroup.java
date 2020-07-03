package com.game.server.eventGroup.game;

import com.game.server.constant.MsgRegionConstant;
import com.game.server.core.groupHelper.EventHandler;
import com.game.server.core.groupHelper.EventThreadGroup;

/**
 * Created by Administrator on 2020/7/3.
 */
public class GameEventGroup extends EventThreadGroup {

    public GameEventGroup(Class<? extends EventHandler> handler, int count) {
        super(handler, count);
    }

    @Override
    protected String getRegionName() {
        return MsgRegionConstant.MSG_REGION_GAME;
    }
}
