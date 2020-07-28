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

    private static Logger logger = Logger.getLogger(SocketReceive.class);
    private final PullSocket mSocket;
    private final int serverKey;
    private ReceiveAdapter receiveAdapter;

    public Receive(String ip,int port,int serverKey) {
        mSocket = new PullSocket();
        mSocket.connect(ip + ":" + port);
        mSocket.setRecvTimeout(30000);
        this.serverKey = serverKey;
        setDaemon(true);
    }

    public void setReceiveAdapter(ReceiveAdapter receiveAdapter) {
        this.receiveAdapter = receiveAdapter;
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
