package core.sql;

import org.nutz.dao.DaoException;
import org.nutz.dao.DaoInterceptor;
import org.nutz.dao.DaoInterceptorChain;
import org.nutz.dao.impl.jdbc.NutPojo;
import org.nutz.dao.impl.sql.NutSql;
import org.nutz.dao.sql.DaoStatement;

public class SqlDaoInterceptor implements DaoInterceptor {

    @Override
    public void filter(DaoInterceptorChain chain) throws DaoException {
        DaoStatement st = chain.getDaoStatement();
        if (st instanceof NutPojo) {
        } else if (st instanceof NutSql) {
        }
        chain.doChain();
    }
}
