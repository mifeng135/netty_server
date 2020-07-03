package com.game.server.eventGroup.db;

import com.game.server.constant.MsgRegionConstant;
import com.game.server.core.groupHelper.EventHandler;
import com.game.server.core.groupHelper.EventThreadGroup;

/**
 * Created by Administrator on 2020/6/27.
 */
public class DBEventGroup extends EventThreadGroup {


    public DBEventGroup(Class<? extends EventHandler> handler, int count) {
        super(handler, count);
    }

    @Override
    protected String getRegionName() {
        return MsgRegionConstant.MSG_REGION_DB;
    }
}
