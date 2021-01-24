package core.group;

/**
 * Created by Administrator on 2020/6/18.
 */
public class EventThreadGroup {

    private final int mThreadCount;
    private MessageGroup mMessageGroup = MessageGroup.getInstance();

    public EventThreadGroup(int count, Class<? extends EventHandler> handler) {
        mThreadCount = count;
        mMessageGroup.setThreadCount(count);
        initMessageGroup();
        initTaskThread(handler);
    }

    public EventThreadGroup(int count) {
        mThreadCount = count;
        mMessageGroup.setThreadCount(count);
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
                messageThreadTask.setName("logic" + i);
                messageThreadTask.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
