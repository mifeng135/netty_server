package com.game.db.eventGroup;

import com.game.db.constant.MsgRegionConstant;
import com.game.db.core.groupHelper.EventHandler;
import com.game.db.core.groupHelper.EventThreadGroup;

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
