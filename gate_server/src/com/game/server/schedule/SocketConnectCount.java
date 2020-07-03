package com.game.server.schedule;


import com.game.server.constant.MsgCmdConstant;
import com.game.server.core.connect.ConnectionManager;
import com.game.server.core.msg.MsgBean;
import com.game.server.proto.LinkSynMsg;
import com.game.server.serverConfig.ServerConfig;
import com.game.server.socket.login.SendToLogin;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2020/6/24.
 */
public class SocketConnectCount {

    /**30秒同步一次链接次数*/
    public SocketConnectCount() {
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);
        scheduledThreadPool.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                int size = ConnectionManager.getConnectSize();

                LinkSynMsg.linkSynS.Builder linkSynS = LinkSynMsg.linkSynS.newBuilder();
                linkSynS.setIp(ServerConfig.SERVER_IP + ":" + ServerConfig.SERVER_PORT);
                linkSynS.setConnectCount(size);
                byte[] data = linkSynS.build().toByteArray();

                MsgBean msgBean = new MsgBean();
                msgBean.setCmd(MsgCmdConstant.MSG_CMD_LINK_SYN_S);
                msgBean.setData(data);
                SendToLogin.getInstance().pushSendMsg(msgBean);
            }
        }, 0, 30, TimeUnit.SECONDS);
    }
}