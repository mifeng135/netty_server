package com.game.login.socket;

import com.game.login.core.annotation.CtrlAnnotation;
import com.game.login.core.msg.MsgBean;
import com.game.login.serverConfig.ServerConfig;
import nanomsg.pipeline.PullSocket;

/**
 * Created by Administrator on 2020/6/24.
 */
public class ReceiveFromGate extends Thread {

    private PullSocket mSocket;


    public ReceiveFromGate() {
        mSocket = new PullSocket();
        mSocket.connect(ServerConfig.GATE_SERVER_IP);
        mSocket.setRecvTimeout(30000);
    }

    @Override
    public void run() {
        while (true) {
            try {
                byte[] data = mSocket.recvBytes();
                MsgBean bean = new MsgBean();
                bean.serializeMsg(data);
                dispatchMsg(bean);
            } catch (Exception e) {

            }
        }
    }

    private void dispatchMsg(MsgBean msgBean) {
        CtrlAnnotation.getInstance().invokeMethod(msgBean.getCmd(), msgBean.getData());
    }
}
