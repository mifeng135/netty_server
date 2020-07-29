package com.game.server.core.zero;

import com.game.server.core.groupHelper.MessageDispatchRegion;
import com.game.server.core.groupHelper.MessageGroup;
import com.game.server.core.msg.MsgBean;
import nanomsg.pipeline.PullSocket;
import org.apache.log4j.Logger;

/**
 * Created by Administrator on 2020/7/28.
 */
public class Receive extends Thread {

    private static Logger logger = Logger.getLogger(Receive.class);


    private final PullSocket mSocket;
    private final byte serverKey;
    private final ReceiveAdapter receiveAdapter;

    public Receive(String ip, int port, byte serverKey, ReceiveAdapter adapter) {
        mSocket = new PullSocket();
        mSocket.connect(ip + ":" + port);
        mSocket.setRecvTimeout(30000);
        this.serverKey = serverKey;
        receiveAdapter = adapter;
        setDaemon(true);
    }

    @Override
    public void run() {
        while (true) {
            try {
                byte[] data = mSocket.recvBytes();
                dispatch(data);
            } catch (Exception e) {

            }
        }
    }

    private void dispatch(byte[] data) {
        MsgBean msgBean = new MsgBean();
        msgBean.setServerKey(serverKey);
        msgBean.serializeMsg(data);
        String regionString = receiveAdapter.getRegionString(msgBean);
        if (regionString != null) {
            MessageGroup messageGroup = MessageDispatchRegion.getInstance().getMessageGroupByTag(regionString);
            String prefix = messageGroup.getPrefix();
            int count = messageGroup.getCount();
            String section = prefix + receiveAdapter.getSectionId(msgBean) % count;
            messageGroup.pushMessageWithTag(section, msgBean);
        }
    }
}
