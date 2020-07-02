package com.game.db.core.zero;

import com.game.db.core.groupHelper.MessageDispatchRegion;
import com.game.db.core.groupHelper.MessageGroup;
import com.game.db.core.msg.MsgBean;
import nanomsg.pipeline.PullSocket;
import org.apache.log4j.Logger;

/**
 * Created by Administrator on 2020/6/12.
 */
public abstract class SocketRecv extends Thread {


    private static Logger logger = Logger.getLogger(SocketRecv.class);

    private PullSocket mSocket;

    public SocketRecv() {
        mSocket = new PullSocket();
        mSocket.connect(getSocketIp());
        mSocket.setRecvTimeout(30000);
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


    protected abstract String getSocketIp();

    protected abstract String getRegionString(int cmd);

    private void dispatch(byte[] data) {

        MsgBean msgBean = new MsgBean();
        msgBean.serializeMsg(data);

        String regionString = getRegionString(msgBean.getCmd());
        if (regionString != null) {
            MessageGroup messageGroup = MessageDispatchRegion.getInstance().getMessageGroupByTag(regionString);
            String prefix = messageGroup.getPrefix();
            int count = messageGroup.getCount();
            String section = prefix + msgBean.getId() % count;
            messageGroup.pushMessageWithTag(section, msgBean);
        }
    }
}
