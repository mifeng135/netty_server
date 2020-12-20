package core.sql;

public class DynamicDataSourceHolder {
    private static ThreadLocal<String> contextHolder = new ThreadLocal<>();
    public static final String DB_MASTER = "master";
    public static final String DB_SLAVE = "slave";


    public static String getDbType() {
        String db = contextHolder.get();
        if (db == null)
            db = DB_MASTER;
        return db;
    }

    /**
     * 设置线程的dbType
     *
     * @param datasource 数据源类型
     */
    public static void setDbType(String datasource) {
        contextHolder.set(datasource);
    }

    /**
     * 清理连接类型
     */
    public static void clearDbType() {
        contextHolder.remove();
    }

    public static boolean isMaster() {
        return DB_MASTER.equals(getDbType());
    }
}
