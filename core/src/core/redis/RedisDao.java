package core.redis;

import core.annotation.redis.RedisA;
import core.annotation.redis.RedisInfo;
import core.sql.BaseBean;
import core.sql.SqlHelper;
import core.util.Ins;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.redisson.Redisson;
import org.redisson.api.*;
import org.redisson.config.Config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static core.Constants.LOCAL_SOCKET_RANGE;


/**
 * Created by Administrator on 2020/6/12.
 * this is safe thread instance
 */
public class RedisDao {

    private static final Config config = new Config();
    private static RedissonClient redissonClient;
    private static final String incr = "_incr";
    private static final String specialTable = "game_player_login_info";
    private Map<String, RedisInfo> classMap;

    private static class DefaultInstance {
        static final RedisDao INSTANCE = new RedisDao();
    }

    public static RedisDao getInstance() {
        return DefaultInstance.INSTANCE;
    }

    private RedisDao() {

    }

    public void init(String ip, String pwd) {
        config.useSingleServer().setAddress(ip);
        config.useSingleServer().setPassword(pwd);
        redissonClient = Redisson.create(config);
        initSqlTableIncrement();
        classMap = RedisA.getInstance().getClassMap();
    }

    public void init(String ip, String pwd, int thread, int nettyThread, int db) {
        config.useSingleServer().setAddress(ip);
        config.useSingleServer().setPassword(pwd);
        config.useSingleServer().setDatabase(db);
        config.setThreads(thread);
        config.setNettyThreads(nettyThread);
        config.setCodec(new FastJsonCodec());
        redissonClient = Redisson.create(config);
        initSqlTableIncrement();
        classMap = RedisA.getInstance().getClassMap();
    }

    public RedissonClient getRedisSon() {
        return redissonClient;
    }

    /************************* MAP BEGIN *******************************/


    public void put(String tableKey, BaseBean bean) {
        RedisInfo redisInfo = classMap.get(tableKey);
        redissonClient.getMap(tableKey).fastPut(bean.getId(), bean);
        if (redisInfo.isImmediately()) {
            Ins.sql().insertOrUpdate(bean);
        }
    }

    public void delete(String tableKey, Object key) {
        redissonClient.getMap(tableKey).fastRemove(key);
    }

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
        putAll(tableKey, mapData);
        return data;
    }

    public void putAll(String tableKey, Map value) {
        redissonClient.getMap(tableKey).putAll(value);
    }

    /************************* SCORE SORT BEGIN *****************************/

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


    public void initSqlTableIncrement() {
        List<RedisInfo> list = RedisA.getInstance().getClassList();
        for (RedisInfo redisInfo : list) {
            if (redisInfo.getIncrName().length() > 0) {
                Sql sql = Ins.sql().sqls().create("table_max.data");
                sql.setVar("tableName", redisInfo.getTableName()).setVar("name", redisInfo.getIncrName());
                sql.setCallback(Sqls.callback.integer());
                int increment = Ins.sql().execute(sql).getInt();
                if (redisInfo.getTableName().equals(specialTable) && increment == 0) {
                    increment = LOCAL_SOCKET_RANGE;
                }
                RAtomicLong rAtomicLong = redissonClient.getAtomicLong(redisInfo.getTableName() + incr);
                rAtomicLong.set(increment);
            }
        }
    }

    public int getNextIncrement(String tableName) {
        return (int) redissonClient.getAtomicLong(tableName + incr).incrementAndGet();
    }
}
