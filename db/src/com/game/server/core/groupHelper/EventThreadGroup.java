package com.game.server.core.groupHelper;

/**
 * Created by Administrator on 2020/6/18.
 */
public class EventThreadGroup {

    private String mPrefix = "";
    /**
     * 开启mThreadCount个线程去顺序处理消息 默认是一个
     */
    private int mThreadCount = 1;

    private MessageGroup mMessageGroup;

    private String regionName;

    public EventThreadGroup(String name, Class<? extends EventHandler> handler, int count) {
        mThreadCount = count;
        regionName = name;
        initMessageGroup();
        initTaskThread(handler);
    }

    private void initMessageGroup() {
        mMessageGroup = new MessageGroup(mThreadCount, mPrefix);
        for (int i = 0; i < mThreadCount; i++) {
            String name = mPrefix + i;
            mMessageGroup.createQueueByTag(name);
        }
        MessageDispatchRegion.getInstance().addRegion(regionName, mMessageGroup);
    }

    private void initTaskThread(Class<? extends EventHandler> handler) {
        for (int i = 0; i < mThreadCount; i++) {
            String name = mPrefix + i;
            try {
                MessageThreadTask messageThreadTask = new MessageThreadTask(mMessageGroup, handler.newInstance(), name);
                messageThreadTask.setDaemon(true);
                messageThreadTask.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
