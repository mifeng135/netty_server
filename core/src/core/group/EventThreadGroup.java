package core.group;

import core.util.Util;

/**
 * Created by Administrator on 2020/6/18.
 */
public class EventThreadGroup {

    private final int mThreadCount;
    private String threadName = "";
    private MessageGroup mMessageGroup = MessageGroup.getInstance();
    private EventHandler eventHandler;

    /**
     * @param count   处理的线程数量
     * @param handler handler
     * @param name    线程名称
     */
    public EventThreadGroup(int count, Class<? extends EventHandler> handler, String name) {
        mThreadCount = count;
        mMessageGroup.setThreadCount(count);
        threadName = name;
        initMessageGroup();
        try {
            eventHandler = handler.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        initTaskThread();
    }

    /***
     *
     * @param count 处理的线程数量
     */
    public EventThreadGroup(int count) {
        mThreadCount = count;
        mMessageGroup.setThreadCount(count);
        initMessageGroup();
        initTaskThread();
    }

    public EventThreadGroup() {
        mThreadCount = Util.getRunProcessor() * 2;
        mMessageGroup.setThreadCount(mThreadCount);
        initMessageGroup();
        initTaskThread();
    }

    /**
     * @param count 处理的线程数量
     * @param name  线程名称
     */
    public EventThreadGroup(int count, String name) {
        mThreadCount = count;
        mMessageGroup.setThreadCount(count);
        threadName = name;
        initMessageGroup();
        initTaskThread();
    }

    private void initMessageGroup() {
        for (int i = 0; i < mThreadCount; i++) {
            mMessageGroup.createQueueByTag(i);
        }
    }

    private void initTaskThread() {
        for (int i = 0; i < mThreadCount; i++) {
            try {
                MessageThreadTask messageThreadTask;
                messageThreadTask = new MessageThreadTask(mMessageGroup, eventHandler, i);
                messageThreadTask.setName(threadName + i);
                messageThreadTask.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
