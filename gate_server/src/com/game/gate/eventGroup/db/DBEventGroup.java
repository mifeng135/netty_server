package com.game.gate.eventGroup.db;

import com.game.gate.constant.MsgRegionConstant;
import com.game.gate.core.groupHelper.EventHandler;
import com.game.gate.core.groupHelper.EventThreadGroup;

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
