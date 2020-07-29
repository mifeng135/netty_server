package com.game.server.eventGroup.login;

import com.game.server.core.groupHelper.EventGroupDispatch;
import com.game.server.serverConfig.ServerConfig;

/**
 * Created by Administrator on 2020/6/23.
 */
public class EventDispatch extends EventGroupDispatch {

    private static class DefaultInstance {
        static final EventDispatch INSTANCE = new EventDispatch();
    }

    public static EventDispatch getInstance() {
        return DefaultInstance.INSTANCE;
    }

    private EventDispatch() {}
    @Override
    protected String getRegionString(int cmd) {
        return ServerConfig.REGION_LOGIN;
    }
}
