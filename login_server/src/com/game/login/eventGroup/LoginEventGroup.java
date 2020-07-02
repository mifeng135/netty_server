package com.game.login.eventGroup;

import com.game.login.core.groupHelper.EventHandler;
import com.game.login.core.groupHelper.EventThreadGroup;
import com.game.login.core.groupHelper.MessageDispatchRegion;
import com.game.login.serverConfig.ServerConfig;

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
