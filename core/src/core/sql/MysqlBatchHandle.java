package core.sql;


import core.annotation.SqlAnnotation;

import java.util.concurrent.*;

/**
 * Created by Administrator on 2020/6/15.
 */
public class MysqlBatchHandle {
    private ConcurrentLinkedQueue<MysqlBean> mMsgQueue = new ConcurrentLinkedQueue();

    private static class DefaultInstance {
        static final MysqlBatchHandle INSTANCE = new MysqlBatchHandle();
    }

    public static MysqlBatchHandle getInstance() {
        return DefaultInstance.INSTANCE;
    }

    private MysqlBatchHandle() {
        init();
    }

    public void pushMsg(MysqlBean bean) {
        mMsgQueue.offer(bean);
    }

    public void init() {

        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);
        scheduledThreadPool.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                boolean empty = mMsgQueue.isEmpty();
                if (!empty) {
                    int size = mMsgQueue.size();
                    for (int i = 0; i < size; i++) {
                        MysqlBean mysqlBean = mMsgQueue.poll();
                        if (mysqlBean == null) {
                            break;
                        }
                        int cmd = mysqlBean.getCmd();
                        Object oc = mysqlBean.getData();
                        SqlAnnotation.getInstance().executeCommitSql(cmd, oc);
                    }
                    //SqlAnnotation.getInstance().executeCommitSqlBatch(mMsgQueue, 1000);
                }
            }
        }, 0, 10, TimeUnit.SECONDS);
    }
}
