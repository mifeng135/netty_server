package com.game.server.controller;

import com.game.server.constant.MsgCmdConstant;
import com.game.server.core.annotation.CtrlCmd;
import com.game.server.core.annotation.Ctrl;
import com.game.server.core.proto.ProtoUtil;
import com.game.server.manager.LinkSynManager;
import com.game.server.proto.ProtoLinkSynR;

/**
 * Created by Administrator on 2020/6/24.
 */

@Ctrl
public class LinkSynController {

    @CtrlCmd(cmd = MsgCmdConstant.MSG_CMD_LINK_SYN_R)
    public void linkSyn(byte[] data) throws Exception {
        ProtoLinkSynR protoLinkSynR = ProtoUtil.deserializer(data,ProtoLinkSynR.class);
        String ip = protoLinkSynR.getIp();
        int count = protoLinkSynR.getConnectCount();
        LinkSynManager.getInstance().putLinkInfo(ip, count);
    }
}
