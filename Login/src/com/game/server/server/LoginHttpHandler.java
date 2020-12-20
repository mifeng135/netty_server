package com.game.server.server;

import com.game.server.ServerConfig;
import core.groupHelper.MessageDispatchRegion;
import core.groupHelper.MessageGroup;
import core.netty.HttpHandler;
import core.proto.TransferMsg;

/**
 * Created by Administrator on 2020/12/5.
 */
public class LoginHttpHandler extends HttpHandler {

    @Override
    public void dispatch(TransferMsg transferMsg) {
        String regionString = ServerConfig.REGION_LOGIN;
        if (regionString != null) {
            MessageGroup messageGroup = MessageDispatchRegion.getInstance().getMessageGroupByTag(regionString);
            String prefix = messageGroup.getPrefix();
            int count = messageGroup.getCount();
            String section = prefix + transferMsg.getPlayerIndex() % count;
            messageGroup.pushMessageWithTag(section, transferMsg);
        }
    }
}
