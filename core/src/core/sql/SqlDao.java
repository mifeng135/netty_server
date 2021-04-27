package core.sql;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import javax.sql.DataSource;

import org.nutz.dao.DaoInterceptor;
import org.nutz.dao.impl.FileSqlManager;
import org.nutz.dao.impl.NutDao;
import org.nutz.lang.Files;
import org.nutz.lang.Lang;
import org.nutz.lang.Mirror;
import org.nutz.lang.Streams;

public class SqlDao {

    protected static SqlDao sqlDao = new SqlDao();
    protected static Class<?> druidFactoryClass;
    private Map<String, NutDao> nutDaoMap = new HashMap<>();
    private NutDao defaultNutDao;

    public static SqlDao getInstance() {
        return sqlDao;
    }

    protected SqlDao() {

    }

    public void initWithConfigList(SqlDaoConfig... configList) {
        for (int i = 0; i < configList.length; i++) {
            SqlDaoConfig config = configList[i];
            try {
                DataSource dataSource = createDataSource(new FileInputStream(Files.findFile(config.getMasterFileName())));
                initNutDao(dataSource, config);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public DataSource createDataSource(InputStream in) throws IOException {
        DataSource dataSource;
        Properties props = new Properties();
        try {
            props.load(in);
            Mirror<?> mirror = Mirror.me(druidFactoryClass);
            dataSource = (DataSource) mirror.invoke(null, "createDataSource", new Object[]{props});
            if (!props.containsKey("maxWait")) {
                Mirror.me(dataSource).setValue(dataSource, "maxWait", 15000);
            }
        } finally {
            Streams.safeClose(in);
        }
        return dataSource;
    }

    private void initNutDao(DataSource dataSource, SqlDaoConfig config) {
        NutDao nutDao = new NutDao(dataSource);

        MFNutDaoRunner mfNutDaoRunner = new MFNutDaoRunner();
        mfNutDaoRunner.setMeta(nutDao.meta());
        nutDao.setRunner(mfNutDaoRunner);
        if (config.getPreSqlName().length() != 0) {
            nutDao.setSqlManager(new FileSqlManager(config.getPreSqlName()));
        }
        DaoInterceptor daoInterceptor = config.getDaoInterceptor();
        if (daoInterceptor != null) {
            nutDao.addInterceptor(daoInterceptor);
        }
        initSlave(config.getSlaveFileList(), mfNutDaoRunner);
        if (config.getSqlKey().equals("default")) {
            defaultNutDao = nutDao;
        }
        nutDaoMap.put(config.getSqlKey(), nutDao);
    }

    private void initSlave(List<String> slave, MFNutDaoRunner runner) {
        if (slave == null || slave.size() <= 0) {
            return;
        }
        List<DataSource> dataSourceList = new ArrayList<>();
        for (int i = 0; i < slave.size(); i++) {
            try {
                DataSource dataSource = this.createDataSource(new FileInputStream(Files.findFile(slave.get(i))));
                dataSourceList.add(dataSource);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        runner.setSlaveDataSourceList(dataSourceList);
    }

    public NutDao getDao(String key) {
        return nutDaoMap.get(key);
    }

    public NutDao getDao() {
        return defaultNutDao;
    }

    public synchronized void close() {
        DataSource dataSource = defaultNutDao.getDataSource();
        if (dataSource != null) {
            try {
                Mirror.me(dataSource).invoke(dataSource, "close", new Object[0]);
            } catch (Throwable var2) {
            }
        }
    }

    static {
        try {
            druidFactoryClass = Lang.loadClass("com.alibaba.druid.pool.DruidDataSourceFactory");
        } catch (ClassNotFoundException var1) {
        }
    }
}
