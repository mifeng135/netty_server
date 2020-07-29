package com.game.server.eventGroup.login;

import com.game.server.core.groupHelper.EventHandler;
import com.game.server.core.groupHelper.EventThreadGroup;
import com.game.server.serverConfig.ServerConfig;

/**
 * Created by Administrator on 2020/6/23.
 */
public class LoginEventGroup extends EventThreadGroup {


    public LoginEventGroup( Class<? extends EventHandler> handler,int count) {
        super(handler,count);
    }

    @Override
    protected String getRegionName() {
        return ServerConfig.REGION_LOGIN;
    }
}
