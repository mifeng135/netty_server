package core.sql;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import javax.sql.DataSource;

import org.springframework.util.ReflectionUtils;

public class DynamicDataSource extends AbstractRoutingDataSource {
    private Integer slaveCount;
    private AtomicInteger counter = new AtomicInteger(-1);
    private List<Object> slaveDataSources = new ArrayList<Object>(0);
    @Override
    protected Object determineCurrentLookupKey() {
        if (DynamicDataSourceHolder.isMaster()) {
            return DynamicDataSourceHolder.getDbType();
        }
        return getSlaveKey();
    }


    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        Field field = ReflectionUtils.findField(AbstractRoutingDataSource.class, "resolvedDataSources");
        field.setAccessible(true);
        try {
            Map<Object, DataSource> resolvedDataSources = (Map<Object, DataSource>) field.get(this);
            this.slaveCount = resolvedDataSources.size() - 1;
            for (Map.Entry<Object, DataSource> entry : resolvedDataSources.entrySet()) {
                if (DynamicDataSourceHolder.DB_MASTER.equals(entry.getKey())) {
                    continue;
                }
                slaveDataSources.add(entry.getKey());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object getSlaveKey() {
        Integer index = counter.incrementAndGet() % slaveCount;
        if (counter.get() > 9999) { // 以免超出Integer范围
            counter.set(-1); // 还原
        }
        return slaveDataSources.get(index);
    }
}
