package core.sql;


import com.conversantmedia.util.concurrent.MultithreadConcurrentQueue;
import core.util.Ins;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SyncSql extends Thread {

    private static Logger logger = LoggerFactory.getLogger(SyncSql.class);

    private MultithreadConcurrentQueue<SqlSyncInfo> queue = new MultithreadConcurrentQueue(2048);
    private volatile boolean quit = false;

    private static class DefaultInstance {
        static final SyncSql INSTANCE = new SyncSql();
    }

    public static SyncSql getInstance() {
        return SyncSql.DefaultInstance.INSTANCE;
    }

    private SyncSql() {

    }

    public void add(SqlSyncInfo sqlSyncInfo) {
        queue.offer(sqlSyncInfo);
    }

    @Override
    public void run() {
        while (!quit) {
            try {
                SqlSyncInfo info = queue.poll();
                if (info != null) {
                    process(info);
                }
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public void quit() {
        quit = true;
        while (!queue.isEmpty()) {
            SqlSyncInfo sqlSyncInfo = queue.poll();
            process(sqlSyncInfo);
        }
    }

    private void process(SqlSyncInfo sqlSyncInfo) {
        String dbName = sqlSyncInfo.getDbName();
        BaseBean bean = sqlSyncInfo.getBean();
        if (dbName.length() > 0) {
            Ins.sql(dbName).insertOrUpdate(bean);
        } else {
            Ins.sql().insertOrUpdate(bean);
        }
    }
}
