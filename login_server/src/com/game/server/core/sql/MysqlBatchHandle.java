package com.game.server.core.sql;

import com.game.server.core.annotation.SqlAnnotation;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2020/6/15.
 */
public class MysqlBatchHandle {
    private ConcurrentLinkedDeque<MysqlBean> mMsgQueue = new ConcurrentLinkedDeque();

    private static class DefaultInstance {
        static final MysqlBatchHandle INSTANCE = new MysqlBatchHandle();
    }

    public static MysqlBatchHandle getInstance() {
        return DefaultInstance.INSTANCE;
    }

    public MysqlBatchHandle() {
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
                int queueSize = mMsgQueue.size();
                for (int i = 0; i < queueSize; i++) {
                    MysqlBean mysqlBean = mMsgQueue.poll();
                    if (mysqlBean != null) {
                        int cmd = mysqlBean.getCmd();
                        Object oc = mysqlBean.getData();
                        SqlAnnotation.getInstance().executeCommitSql(cmd, oc);
                    }
                }
            }
        }, 0, 30, TimeUnit.SECONDS);
    }
}
