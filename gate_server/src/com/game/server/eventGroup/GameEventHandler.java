package com.game.server.eventGroup;

import com.game.server.constant.MsgCmdConstant;
import com.game.server.core.connect.ConnectionManager;
import com.game.server.core.groupHelper.EventHandler;
import com.game.server.core.msg.MsgBean;
import io.netty.buffer.ByteBuf;

import java.util.List;

/**
 * Created by Administrator on 2020/7/3.
 */
public class GameEventHandler implements EventHandler {

    @Override
    public void onEvent(MsgBean msgBean) {
        if (msgBean.getSubCmd() == MsgCmdConstant.MSG_BROAD_CASE) {
            ByteBuf sendBuf = msgBean.packClientMsg();
            ConnectionManager.send2AllClient(sendBuf);
        } else if (msgBean.getSubCmd() == MsgCmdConstant.MSG_BROAD_CASE_GROUP) {
            List<Integer> sendList = msgBean.getArrayData();
            for (int i = 0; i < sendList.size(); i++) {
                ConnectionManager.send2ClientByFD(sendList.get(i), msgBean.packClientMsg());
            }
        } else {
            ConnectionManager.send2ClientByFD(msgBean.getId(), msgBean.packClientMsg());
        }
    }
}
