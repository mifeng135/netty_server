package com.game.server.controller;

import com.game.server.constant.MsgCmdConstant;
import com.game.server.core.annotation.CtrlCmd;
import com.game.server.core.annotation.Ctrl;
import com.game.server.manager.LinkSynManager;
import com.game.server.proto.LinkSynMsg;
import com.google.protobuf.InvalidProtocolBufferException;

/**
 * Created by Administrator on 2020/6/24.
 */

@Ctrl
public class LinkSynController {

    @CtrlCmd(cmd = MsgCmdConstant.MSG_CMD_LINK_SYN_R)
    public void linkSyn(byte[] data) throws InvalidProtocolBufferException {
        LinkSynMsg.linkSynR linkSynR = LinkSynMsg.linkSynR.parseFrom(data);

        String ip = linkSynR.getIp();
        int count = linkSynR.getConnectCount();
        LinkSynManager.getInstance().putLinkInfo(ip, count);
    }
}
