package core.sql;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.sql.DataSource;

import org.nutz.dao.impl.FileSqlManager;
import org.nutz.dao.impl.NutDao;
import org.nutz.lang.Files;
import org.nutz.lang.Lang;
import org.nutz.lang.Mirror;
import org.nutz.lang.Streams;

public class SqlDao {

    protected static SqlDao sqlDao = new SqlDao("_defult_");
    protected static Class<?> druidFactoryClass;
    protected String name;
    private String prepareSqlFileName = "";
    private NutDao nutDao;
    private MFNutDaoRunner mfNutDaoRunner;

    public static SqlDao getInstance() {
        return sqlDao;
    }

    protected SqlDao(String name) {
        this.name = name;
    }

    public void setDataSource(DataSource dataSource) {
        nutDao = new NutDao(dataSource);
        mfNutDaoRunner = new MFNutDaoRunner();
        mfNutDaoRunner.setMeta(nutDao.meta());
        nutDao.setRunner(mfNutDaoRunner);
        if (prepareSqlFileName.length() != 0) {
            nutDao.setSqlManager(new FileSqlManager(prepareSqlFileName));
        }
    }

    public void init(String name, List<String> slave) {
        try {
            DataSource dataSource = this.init(new FileInputStream(Files.findFile(name)));
            setDataSource(dataSource);
            initSlave(slave);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init(String name, String preSqlName, List<String> slave) {
        try {
            this.prepareSqlFileName = preSqlName;
            DataSource dataSource = this.init(new FileInputStream(Files.findFile(name)));
            setDataSource(dataSource);
            initSlave(slave);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DataSource init(InputStream in) throws IOException {
        DataSource dataSource;
        Properties props = new Properties();
        try {
            props.load(in);
            dataSource = createDataSource(props);
        } finally {
            Streams.safeClose(in);
        }
        return dataSource;
    }

    private void initSlave(List<String> slave) {
        if (slave == null || slave.size() <= 0) {
            return;
        }
        List<DataSource> dataSourceList = new ArrayList<>();
        for (int i = 0; i < slave.size(); i++) {
            try {
                DataSource dataSource = this.init(new FileInputStream(Files.findFile(slave.get(i))));
                dataSourceList.add(dataSource);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mfNutDaoRunner.setSlaveDataSourceList(dataSourceList);
    }

    public DataSource createDataSource(Properties props) {
        Mirror<?> mirror = Mirror.me(druidFactoryClass);
        DataSource ds = (DataSource) mirror.invoke(null, "createDataSource", new Object[]{props});
        if (!props.containsKey("maxWait")) {
            Mirror.me(ds).setValue(ds, "maxWait", 15000);
        }
        return ds;
    }

    public NutDao getDao() {
        return nutDao;
    }

    public synchronized void close() {
        DataSource dataSource = nutDao.getDataSource();
        if (dataSource != null) {
            try {
                Mirror.me(dataSource).invoke(dataSource, "close", new Object[0]);
            } catch (Throwable var2) {
            }
        }

        List<DataSource> slaveList = mfNutDaoRunner.getSlaveDataSourceList();
        if (slaveList != null) {
            for (int i = 0; i < slaveList.size(); i++) {
                try {
                    Mirror.me(dataSource).invoke(slaveList.get(i), "close", new Object[0]);
                } catch (Throwable var2) {
                }
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
