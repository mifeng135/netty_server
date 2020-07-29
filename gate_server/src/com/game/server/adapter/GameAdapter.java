package com.game.server.adapter;

import com.game.server.constant.MsgRegionConstant;
import com.game.server.core.msg.MsgBean;
import com.game.server.core.zero.ReceiveAdapter;

/**
 * Created by Administrator on 2020/7/29.
 */
public class GameAdapter implements ReceiveAdapter {

    @Override
    public int getSectionId(MsgBean msgBean) {
        return msgBean.getId();
    }

    @Override
    public String getRegionString(MsgBean msgBean) {
        return MsgRegionConstant.MSG_REGION_GAME;
    }
}
