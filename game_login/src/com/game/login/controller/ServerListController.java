package com.game.login.controller;

import bean.login.LoginPlayerInfoBean;
import constants.RedisTableKey;
import core.annotation.Ctrl;
import core.annotation.CtrlCmd;
import core.msg.TransferMsg;
import core.util.Ins;
import core.util.ProtoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protocal.remote.login.GetServerListReq;

import static constants.MsgConstant.*;


/***
 * 获取服务器列表
 */
@Ctrl
public class ServerListController {

    private static Logger logger = LoggerFactory.getLogger(ServerListController.class);

    @CtrlCmd(cmd = MSG_SERVER_LIST_REQ)
    public void getServerList(TransferMsg msg) {
        GetServerListReq serverListReq = ProtoUtil.deserializer(msg.getData(), GetServerListReq.class);
        String openId = serverListReq.getOpenId();
        process(openId, msg);
    }

    @CtrlCmd(httpCmd = HTTP_URL_GET_SERVER_LIST)
    public void httpGetServerList(TransferMsg msg) {
        String openId = msg.getParams().get("openId");
        process(openId, msg);
    }

    private void process(String openId, TransferMsg msg) {
        LoginPlayerInfoBean bean = Ins.redis().fetch(RedisTableKey.GAME_PLAYER_LOGIN_INFO, openId);
        if (bean != null) {

        }
    }
}
