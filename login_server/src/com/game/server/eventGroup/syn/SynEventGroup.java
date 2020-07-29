package com.game.server.eventGroup.syn;

import com.game.server.core.groupHelper.EventHandler;
import com.game.server.core.groupHelper.EventThreadGroup;
import com.game.server.serverConfig.ServerConfig;

/**
 * Created by Administrator on 2020/7/29.
 */
public class SynEventGroup extends EventThreadGroup {

    public SynEventGroup(Class<? extends EventHandler> handler, int count) {
        super(handler, count);
    }

    @Override
    protected String getRegionName() {
        return ServerConfig.REGION_SYN;
    }
}
