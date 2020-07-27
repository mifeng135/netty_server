package com.game.server.core.groupHelper;

import com.game.server.core.msg.MsgBean;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Administrator on 2020/6/23.
 */
public abstract class EventGroupDispatch extends Thread {

    private ConcurrentLinkedQueue<MsgBean> mMsgQueue = new ConcurrentLinkedQueue();

    public EventGroupDispatch() {
    }
    public void pushMsg(MsgBean bean) {
        mMsgQueue.offer(bean);
    }

    protected abstract String getRegionString(int cmd);
    @Override
    public void run() {
        while (true) {
            try {
                for (int i = 0; i < 100; i++) {
                    MsgBean msgBean = mMsgQueue.poll();
                    if (msgBean == null) {
                        break;
                    }
                    dispatch(msgBean);
                }
                Thread.sleep(10);
            } catch (Exception e) {

            }
        }
    }


    private void dispatch(MsgBean msgBean) {
        String regionString = getRegionString(msgBean.getCmd());
        if (regionString != null) {
            MessageGroup messageGroup = MessageDispatchRegion.getInstance().getMessageGroupByTag(regionString);
            String prefix = messageGroup.getPrefix();
            int count = messageGroup.getCount();
            String section = prefix + msgBean.getFd() % count;
            messageGroup.pushMessageWithTag(section, msgBean);
        }
    }
}
