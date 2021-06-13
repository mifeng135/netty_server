package com.game.login.controller;

import bean.login.LoginPlayerInfoBean;
import bean.login.ServerListInfoBean;
import com.game.login.util.HttpUtil;
import constants.TableKey;
import core.annotation.ctrl.Ctrl;
import core.annotation.ctrl.CtrlCmd;
import core.msg.TransferMsg;
import core.util.Ins;
import core.util.ProtoUtil;
import core.util.TimeUtil;
import core.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protocal.remote.login.ServerListReq;
import protocal.remote.login.ServerListRsp;

import java.util.List;

import static constants.MsgConstant.*;


/***
 * 获取服务器列表
 */
@Ctrl
public class ServerListController {

    private static Logger logger = LoggerFactory.getLogger(ServerListController.class);

    @CtrlCmd(cmd = MSG_SERVER_LIST_REQ)
    public void getServerList(TransferMsg msg) {
        ServerListReq serverListReq = ProtoUtil.deserializer(msg.getData(), ServerListReq.class);
        String openId = serverListReq.getOpenId();
        process(openId, msg);
    }

    @CtrlCmd(httpCmd = HTTP_URL_GET_SERVER_LIST)
    public void httpGetServerList(TransferMsg msg) {
        String openId = msg.getParams().get("openId");
        process(openId, msg);
    }

    private void process(String openId, TransferMsg msg) {
        LoginPlayerInfoBean bean = Ins.redis().fetch(TableKey.GAME_PLAYER_LOGIN_INFO, openId);
        if (bean == null) {
            bean = new LoginPlayerInfoBean();
            bean.setId(openId);
            bean.setCreateTime(TimeUtil.getCurrentTimeSecond());
            bean.setPlayerIndex(Ins.redis().getNextIncrement(TableKey.GAME_PLAYER_LOGIN_INFO));
            Ins.redis().put(TableKey.GAME_PLAYER_LOGIN_INFO, bean);
        }
        List<ServerListInfoBean> listInfoBeanList = Ins.redis().fetchAll(TableKey.GAME_SERVER_LIST);
        ServerListRsp serverListRsp = new ServerListRsp();
        serverListRsp.setServerList(listInfoBeanList);
        serverListRsp.setPlayerIndex(bean.getPlayerIndex());
        serverListRsp.setSelfServerList(Util.mapToList(bean.decode()));
        HttpUtil.sendMsg(msg, MSG_SERVER_LIST_RSP, serverListRsp);
    }
}
