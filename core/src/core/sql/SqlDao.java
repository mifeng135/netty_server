package core.sql;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
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
    private Map<Integer, NutDao> daoMap = new HashMap<>();
    private String prepareSqlFileName = "";

    public static SqlDao getInstance() {
        return sqlDao;
    }

    protected SqlDao(String name) {
        this.name = name;
    }

    public void setDataSource(int type, DataSource dataSource) {
        NutDao nutDao = new NutDao(dataSource);
        if (prepareSqlFileName.length() == 0) {
            daoMap.put(type, nutDao);
        } else {
            nutDao.setSqlManager(new FileSqlManager(prepareSqlFileName));
            daoMap.put(type, nutDao);
        }
    }

    public void init(int type, String name) {
        try {
            this.init(type, new FileInputStream(Files.findFile(name)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init(int type, String name, String preSqlName) {
        try {
            this.prepareSqlFileName = preSqlName;
            this.init(type, new FileInputStream(Files.findFile(name)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init(int type, InputStream in) throws IOException {
        Properties props = new Properties();
        try {
            props.load(in);
            this.init(type, props);
        } finally {
            Streams.safeClose(in);
        }
    }

    public void init(int type, Properties props) {
        DataSource ds = this.buildDataSource(type, props);
        this.setDataSource(type, ds);
    }

    protected DataSource buildDataSource(int type, Properties props) {
        Mirror<?> mirror = Mirror.me(druidFactoryClass);
        DataSource ds = (DataSource) mirror.invoke((Object) null, "createDataSource", new Object[]{props});
        if (!props.containsKey("maxWait")) {
            Mirror.me(ds).setValue(ds, "maxWait", 15000);
        }
        return ds;
    }

    public NutDao getDao(int type) {
        return daoMap.get(type);
    }

    public synchronized void close(int type) {
        NutDao dao = daoMap.remove(type);
        DataSource dataSource = dao.getDataSource();
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
