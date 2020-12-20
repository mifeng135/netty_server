package core.sql;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Locale;
import java.util.Properties;


@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
})

public class DynamicDataSourceInterceptor implements Interceptor {

    private static final String REGEX = ".*insert\\u0020.*|.*delete\\u0020.*|.*update\\u0020.*";

    public Object intercept(Invocation invocation) throws Throwable {
        boolean active = TransactionSynchronizationManager.isActualTransactionActive();
        Object[] objects = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) objects[0];
        String lookupKey = DynamicDataSourceHolder.DB_MASTER;

        if (active != true) {
            if (mappedStatement.getSqlCommandType().equals(SqlCommandType.SELECT)) {
                if (mappedStatement.getId().contains(SelectKeyGenerator.SELECT_KEY_SUFFIX)) {
                    lookupKey = DynamicDataSourceHolder.DB_MASTER;
                } else {
                    BoundSql boundSql = mappedStatement.getSqlSource().getBoundSql(objects[1]);
                    String sql = boundSql.getSql().toLowerCase(Locale.CHINA).replaceAll("[\\t\\n\\r]", " ");
                    if (sql.matches(REGEX)) {
                        lookupKey = DynamicDataSourceHolder.DB_MASTER;
                    } else {
                        lookupKey = DynamicDataSourceHolder.DB_SLAVE;
                    }
                }
            }
        } else {
            lookupKey = DynamicDataSourceHolder.DB_MASTER;
        }
        DynamicDataSourceHolder.setDbType(lookupKey);
        return invocation.proceed();
    }

    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    public void setProperties(Properties properties) {
    }
}
