package com.game.server.adapter;

import com.game.server.core.msg.MsgBean;
import com.game.server.core.zero.ReceiveAdapter;
import com.game.server.serverConfig.ServerConfig;

/**
 * Created by Administrator on 2020/7/29.
 */
public class SynAdapter implements ReceiveAdapter {
    @Override
    public int getSectionId(MsgBean msgBean) {
        return msgBean.getId();
    }

    @Override
    public String getRegionString(MsgBean msgBean) {
        return ServerConfig.REGION_SYN;
    }
}
