package com.game.server.eventGroup.game;

import com.game.server.constant.MsgCmdConstant;
import com.game.server.core.connect.ConnectionManager;
import com.game.server.core.groupHelper.EventHandler;
import com.game.server.core.msg.MsgBean;
import io.netty.buffer.ByteBuf;

/**
 * Created by Administrator on 2020/7/3.
 */
public class GameEventHandler implements EventHandler {

    @Override
    public void onEvent(MsgBean msgBean) {
        if (msgBean.getSubCmd() == MsgCmdConstant.MSG_BROAD_CASE) {
            ByteBuf sendBuf = msgBean.packClientMsg();
            ConnectionManager.send2AllClient(sendBuf);
        } else {
            ConnectionManager.send2ClientByFD(msgBean.getId(), msgBean.packClientMsg());
        }
    }
}
