package core.redis;

import core.annotation.redis.RedisA;
import core.annotation.redis.RedisInfo;
import core.sql.*;
import core.util.Ins;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.redisson.Redisson;
import org.redisson.api.*;
import org.redisson.config.Config;
import org.redisson.config.MasterSlaveServersConfig;

import java.util.*;
import java.util.stream.Collectors;

import static core.Constants.LOCAL_SOCKET_RANGE;


/**
 * Created by Administrator on 2020/6/12.
 * this is safe thread instance
 */
public class RedisDao {

    private static RedissonClient redissonClient;
    private static final String incr = "_incr";
    private static final String specialTable = "game_player_login_info";
    private Map<String, RedisInfo> classMap;
    private Map<String, RedissonClient> clientMap = new HashMap<>();


    private static class DefaultInstance {
        static final RedisDao INSTANCE = new RedisDao();
    }

    public static RedisDao getInstance() {
        return DefaultInstance.INSTANCE;
    }

    private RedisDao() {
    }

    public void init(String dbName, RedisConfig... redisConfigList) {
        for (RedisConfig redisConfig : redisConfigList) {
            Config config = new Config();
            String pwd = redisConfig.getPwd();
            int db = redisConfig.getDb();
            MasterSlaveServersConfig masterSlaveServersConfig = config.useMasterSlaveServers();
            masterSlaveServersConfig.setMasterAddress(redisConfig.getMasterStr()).setPassword(pwd).setDatabase(db);
            List<String> slaveList = redisConfig.getSlaveStr();
            if (slaveList != null) {
                for (String str : slaveList) {
                    masterSlaveServersConfig.addSlaveAddress(str).setPassword(pwd).setDatabase(db);
                }
            }
            config.setThreads(redisConfig.getThread());
            config.setNettyThreads(redisConfig.getNettyThread());
            config.setCodec(new FastJsonCodec());
            RedissonClient client = Redisson.create(config);
            if (redisConfig.getKey().equals("default")) {
                redissonClient = client;
            }
            clientMap.put(redisConfig.getKey(), client);
        }
        classMap = new RedisA(dbName).getClassMap();
        initSqlTableIncrement();
    }

    /**
     * 指定数据库 插入
     *
     * @param daoKey   数据库名称
     * @param tableKey 表名
     * @param bean     数据bean
     */
    public void put(String daoKey, String tableKey, BaseBean bean) {
        RedissonClient client = clientMap.get(daoKey);
        RedisInfo redisInfo = classMap.get(tableKey);
        client.getMap(tableKey).fastPut(bean.getId(), bean);
        if (redisInfo.isImmediately()) {
            Ins.sql(daoKey).insertOrUpdate(bean);
        } else {
            SqlSyncInfo sqlSyncInfo = new SqlSyncInfo();
            sqlSyncInfo.setBean(bean);
            sqlSyncInfo.setDbName(daoKey);
            sqlSyncInfo.setTableKey(tableKey);
            sqlSyncInfo.setDelete(redisInfo.isDelete());
            SyncSql.getInstance().add(sqlSyncInfo);
        }
    }


    /**
     * 通过默认的数据库插入
     *
     * @param tableKey
     * @param bean
     */
    public void put(String tableKey, BaseBean bean) {
        RedisInfo redisInfo = classMap.get(tableKey);
        redissonClient.getMap(tableKey).fastPut(bean.getId(), bean);
        if (redisInfo.isImmediately()) {
            Ins.sql().insertOrUpdate(bean);
        } else {
            SqlSyncInfo sqlSyncInfo = new SqlSyncInfo();
            sqlSyncInfo.setBean(bean);
            sqlSyncInfo.setTableKey(tableKey);
            sqlSyncInfo.setDelete(redisInfo.isDelete());
            SyncSql.getInstance().add(sqlSyncInfo);
        }
    }


    /**
     * 删除 某个表名下的 id为key的数据
     *
     * @param tableKey 表名
     * @param key      id
     */
    public void delete(String tableKey, Object key) {
        redissonClient.getMap(tableKey).fastRemove(key);
        Class cls = classMap.get(tableKey).getCls();
        Ins.sql().clear(cls, Cnd.where("id", "=", key));
    }

    /**
     * 查询 某个表名下 id为 key的数据
     *
     * @param tableKey 表名
     * @param key      id
     * @param <T>
     * @return
     */
    public <T> T fetch(String tableKey, Object key) {
        Object data = redissonClient.getMap(tableKey).get(key);
        if (data == null) {
            Class cls = classMap.get(tableKey).getCls();
            data = Ins.sql().fetch(cls, Cnd.where("id", "=", key));
            if (data != null) {
                redissonClient.getMap(tableKey).put(key, data);
            }
        }
        return (T) data;
    }

    /**
     * 询 某个表名下的所有数据
     *
     * @param tableKey
     * @return
     */
    public List fetchAll(String tableKey) {
        RMap map = redissonClient.getMap(tableKey);
        int cacheCount = map.size();
        int tableCount = SqlHelper.getTableCount(tableKey);
        if (cacheCount >= tableCount) {
            return new ArrayList(map.values());
        }
        Class cls = classMap.get(tableKey).getCls();
        List<BaseBean> data = Ins.sql().query(cls, null);
        Map mapData = data.stream().collect(Collectors.toMap(BaseBean::getId, (p) -> p));
        map.putAll(mapData);
        return data;
    }

    public void scoreSetAdd(String mapKey, double score, Object o) {
        RScoredSortedSet set = redissonClient.getScoredSortedSet(mapKey);
        Double old = set.getScore(o);
        if (old == null) {
            set.add(score, o);
        } else {
            set.add(score + old, o);
        }
    }

    public Collection scoreSetGet(String mapKey, int endIndex) {
        return redissonClient.getScoredSortedSet(mapKey).valueRangeReversed(0, endIndex - 1);
    }

    public Collection scoreSetGet(String mapKey, int startIndex, int endIndex) {
        return redissonClient.getScoredSortedSet(mapKey).valueRangeReversed(startIndex - 1, endIndex - 1);
    }

    public Integer scoreSetRank(String mapKey, Object o) {
        return redissonClient.getScoredSortedSet(mapKey).revRank(o) + 1;
    }

    public void scoreSetClear(String mapKey) {
        redissonClient.getScoredSortedSet(mapKey).clear();
    }

    /**
     * 初始化sql 某人自增长id
     */
    public void initSqlTableIncrement() {
        List<RedisInfo> list = new ArrayList<>(classMap.values());
        for (RedisInfo redisInfo : list) {
            if (redisInfo.getIncrName().length() > 0) {
                Sql sql = Ins.sql().sqls().create("table_max.data");
                sql.setVar("tableName", redisInfo.getTableName()).setVar("name", redisInfo.getIncrName());
                sql.setCallback(Sqls.callback.integer());
                int increment = Ins.sql().execute(sql).getInt();
                if (increment == 0) {
                    increment = redisInfo.getIncr();
                }
                RAtomicLong rAtomicLong = redissonClient.getAtomicLong(redisInfo.getTableName() + incr);
                rAtomicLong.set(increment);
            }
        }
    }

    /**
     * 获取某个表名的下一个自增长id
     *
     * @param tableName
     * @return
     */
    public int getNextIncrement(String tableName) {
        return (int) redissonClient.getAtomicLong(tableName + incr).incrementAndGet();
    }
}
