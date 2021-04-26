package core.group;

/**
 * Created by Administrator on 2020/6/18.
 */
public class EventThreadGroup {

    private final int mThreadCount;
    private String threadName = "";
    private MessageGroup mMessageGroup = MessageGroup.getInstance();


    /**
     *
     * @param count 处理的线程数量
     * @param handler handler
     * @param name 线程名称
     */
    public EventThreadGroup(int count, Class<? extends EventHandler> handler, String name) {
        mThreadCount = count;
        mMessageGroup.setThreadCount(count);
        threadName = name;
        initMessageGroup();
        initTaskThread(handler);
    }

    /***
     *
     * @param count 处理的线程数量
     */
    public EventThreadGroup(int count) {
        mThreadCount = count;
        mMessageGroup.setThreadCount(count);
        initMessageGroup();
        initTaskThread(null);
    }


    /**
     *
     * @param count 处理的线程数量
     * @param name 线程名称
     */
    public EventThreadGroup(int count, String name) {
        mThreadCount = count;
        mMessageGroup.setThreadCount(count);
        threadName = name;
        initMessageGroup();
        initTaskThread(null);
    }

    private void initMessageGroup() {
        for (int i = 0; i < mThreadCount; i++) {
            mMessageGroup.createQueueByTag(i);
        }
    }

    private void initTaskThread(Class<? extends EventHandler> handler) {
        for (int i = 0; i < mThreadCount; i++) {
            try {
                MessageThreadTask messageThreadTask;
                if (handler == null) {
                    messageThreadTask = new MessageThreadTask(mMessageGroup, null, i);
                } else {
                    messageThreadTask = new MessageThreadTask(mMessageGroup, handler.newInstance(), i);
                }
                messageThreadTask.setName(threadName + i);
                messageThreadTask.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
