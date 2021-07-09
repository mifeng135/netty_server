package core.sql;

import core.util.Ins;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;


public class SyncSql {

    private static Logger logger = LoggerFactory.getLogger(SyncSql.class);

    ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

    private static class DefaultInstance {
        static final SyncSql INSTANCE = new SyncSql();
    }

    public static SyncSql getInstance() {
        return SyncSql.DefaultInstance.INSTANCE;
    }

    private SyncSql() {

    }

    public void add(SqlSyncInfo sqlSyncInfo) {
        singleThreadExecutor.submit(new DBTask(sqlSyncInfo));
    }

    private class DBTask implements Runnable {

        private final SqlSyncInfo info;

        public DBTask(SqlSyncInfo info) {
            this.info = info;
        }

        @Override
        public void run() {
            process(info);
        }

        private void process(SqlSyncInfo sqlSyncInfo) {
            String dbName = sqlSyncInfo.getDbName();
            BaseBean bean = sqlSyncInfo.getBean();
            try {
                if (dbName != null && dbName.length() > 0) {
                    Ins.sql(dbName).insertOrUpdate(bean);
                } else {
                    Ins.sql().insertOrUpdate(bean);
                }
            } catch (Exception e) {
                logger.error(e.toString());
            }
        }
    }
}
