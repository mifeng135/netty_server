package com.game.game.core.zero;

import com.game.game.core.groupHelper.MessageDispatchRegion;
import com.game.game.core.groupHelper.MessageGroup;
import com.game.game.core.msg.MsgBean;
import nanomsg.pipeline.PullSocket;
import org.apache.log4j.Logger;

/**
 * Created by Administrator on 2020/6/12.
 */
public abstract class SocketReceive extends Thread {

    private static Logger logger = Logger.getLogger(SocketReceive.class);
    private final PullSocket mSocket;

    public SocketReceive() {
        mSocket = new PullSocket();
        mSocket.connect(getSocketIp());
        mSocket.setRecvTimeout(30000);
        setDaemon(true);
    }

    /**
     * setting consumer ip
     *
     * @return String
     */
    protected abstract String getSocketIp();

    /**
     * return region string by cmd
     *
     * @param cmd
     * @return
     */
    protected abstract String getRegionString(int cmd);

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
