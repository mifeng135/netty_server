package core.sql;

import org.nutz.dao.ConnCallback;
import org.nutz.dao.DaoException;
import org.nutz.dao.DaoInterceptorChain;
import org.nutz.dao.DatabaseMeta;
import org.nutz.dao.impl.DaoRunner;
import org.nutz.dao.sql.DaoStatement;
import org.nutz.lang.Configurable;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.trans.Atom;
import org.nutz.trans.Trans;
import org.nutz.trans.Transaction;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.List;

public class MFNutDaoRunner implements DaoRunner, Configurable {

    private static final Log log = Logs.get();


    protected List<DataSource> slaveDataSourceList;

    protected boolean supportSavePoint = true;

    public void run(final DataSource dataSource, final ConnCallback callback) {
        if (callback instanceof DaoInterceptorChain) {
            DaoInterceptorChain chain = (DaoInterceptorChain) callback;
            DaoStatement[] sts = chain.getDaoStatements();
            boolean useTrans = false;
            boolean isAllSelect = true;
            for (DaoStatement st : sts) {
                if (!st.isSelect() && !st.isForceExecQuery()) {
                    isAllSelect = false;
                    break;
                }
            }
            switch (meta.getType()) {
                case PSQL:
                    useTrans = true;
                    break;
                case SQLITE:
                    Transaction t = Trans.get();
                    if (t == null) {
                        if (isAllSelect)
                            useTrans = false;
                        else {
                            chain.setAutoTransLevel(Connection.TRANSACTION_READ_UNCOMMITTED);
                            useTrans = true;
                        }
                    } else if (t.getLevel() != Connection.TRANSACTION_SERIALIZABLE
                            && t.getLevel() != Connection.TRANSACTION_READ_UNCOMMITTED) {
                        t.setLevel(Connection.TRANSACTION_READ_UNCOMMITTED);
                        useTrans = true;
                    }
                    break;
                default:
                    useTrans = !(Trans.isTransactionNone() && (sts.length == 1 || isAllSelect));
                    break;
            }
            if (useTrans && chain.getAutoTransLevel() > 0) {
                Trans.exec(chain.getAutoTransLevel(), new Atom() {
                    public void run() {
                        _run(dataSource, callback);
                    }
                });
                return;
            }
        }
        _run(dataSource, callback);
    }

    public void _run(DataSource dataSource, ConnCallback callback) {
        Transaction t = Trans.get();
        if (null != t) {
            _runWithTransaction(t, dataSource, callback);
        }
        else {
            _runWithoutTransaction(dataSource, callback);
        }
    }

    protected void _runWithTransaction(Transaction t, DataSource dataSource, ConnCallback callback) {
        Connection conn = null;
        Savepoint sp = null;
        try {
            conn = t.getConnection(selectDataSource(t, dataSource, callback));
            if (supportSavePoint && meta != null && meta.isPostgresql()) {
                sp = conn.setSavepoint();
            }
            runCallback(conn, callback);
        } catch (Exception e) {
            if (sp != null && conn != null)
                try {
                    conn.rollback(sp);
                } catch (SQLException e1) {
                }
            if (e instanceof DaoException)
                throw (DaoException) e;
            throw new DaoException(e);
        }
    }

    public void _runWithoutTransaction(DataSource dataSource, ConnCallback callback) {
        Connection conn = null;
        try {
            conn = selectDataSource(null, dataSource, callback).getConnection();
            runCallback(conn, callback);
            if (!conn.getAutoCommit()) {
                conn.commit();
            }
        }
        catch (Exception e) {
            try {
                if (conn != null)  {
                    conn.rollback();
                }
            } catch (Exception e1) {
            }
            if (e instanceof DaoException) {
                throw (DaoException) e;
            }
            throw new DaoException(e);
        }
        finally {
            if (null != conn) {
                try {
                    conn.close();
                } catch (SQLException closeE) {
                    if (log.isWarnEnabled()) {
                        log.warn("Fail to close connection!", closeE);
                    }
                }
            }
        }
    }


    protected void runCallback(Connection conn, ConnCallback callback) throws Exception {
        callback.invoke(conn);
    }

    protected DatabaseMeta meta;

    public void setMeta(DatabaseMeta meta) {
        this.meta = meta;
    }

    public void setSlaveDataSourceList(List<DataSource> slaveDataSource) {
        this.slaveDataSourceList = slaveDataSource;
    }

    public List<DataSource> getSlaveDataSourceList() {
        return slaveDataSourceList;
    }

    protected DataSource selectDataSource(Transaction t, DataSource master, ConnCallback callback) {
        if (this.slaveDataSourceList == null) {
            return master;
        }
        if (t == null && callback instanceof DaoInterceptorChain) {
            DaoInterceptorChain chain = (DaoInterceptorChain) callback;
            DaoStatement[] sts = chain.getDaoStatements();
            if (sts.length == 1 && (sts[0].isSelect() || sts[0].isForceExecQuery())) {
                long threadId = Thread.currentThread().getId() % this.slaveDataSourceList.size();
                return slaveDataSourceList.get((int) threadId);
            }
        }
        return master;
    }

    @Override
    public void setupProperties(NutMap conf) {
        supportSavePoint = conf.getBoolean("nutz.dao.jdbc.psql.supportSavePoint", true);
    }
}
