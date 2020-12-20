package com.game.server.adapter;

import com.game.server.ServerConfig;
import core.proto.TransferMsg;
import core.zero.MSocketAdapter;

/**
 * Created by Administrator on 2020/12/13.
 */
public class GameAdapter implements MSocketAdapter {
    @Override
    public int getSectionId(TransferMsg msg) {
        return msg.getPlayerIndex();
    }

    @Override
    public String getRegionString(TransferMsg msg) {
        return ServerConfig.REGION_GAME;
    }
}
