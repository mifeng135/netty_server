package com.game.game.eventGroup.db;

import com.game.game.constant.MsgRegionConstant;
import com.game.game.core.groupHelper.EventHandler;
import com.game.game.core.groupHelper.EventThreadGroup;

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
