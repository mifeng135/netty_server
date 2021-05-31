package core.redis;

import org.redisson.Redisson;
import org.redisson.api.*;
import org.redisson.client.codec.BaseCodec;
import org.redisson.codec.FstCodec;
import org.redisson.config.Config;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static core.Constants.LOCAL_SOCKET_RANGE;

/**
 * Created by Administrator on 2020/6/12.
 * this is safe thread instance
 */
public class RedisManager {

    private static Config config = new Config();
    private static RedissonClient redissonClient;

    private static class DefaultInstance {
        static final RedisManager INSTANCE = new RedisManager();
    }

    public static RedisManager getInstance() {
        return DefaultInstance.INSTANCE;
    }

    private RedisManager() {

    }

    public void init(String ip, String pwd) {
        config.useSingleServer().setAddress(ip);
        config.useSingleServer().setPassword(pwd);
        redissonClient = Redisson.create(config);
        initIndex();
    }

    public void init(String ip, String pwd, int thread, int nettyThread, int db) {
        config.useSingleServer().setAddress(ip);
        config.useSingleServer().setPassword(pwd);
        config.useSingleServer().setDatabase(db);
        config.setThreads(thread);
        config.setNettyThreads(nettyThread);
        config.setCodec(new FstCodec());
        redissonClient = Redisson.create(config);
        initIndex();
    }

    public RedissonClient getRedisSon() {
        return redissonClient;
    }

    /************************* MAP BEGIN *******************************/
    public <K, V> void mapPut(String mapKey, K key, V value) {
        redissonClient.getMap(mapKey).fastPut(key, value);
    }

    public void mapFastRemove(String mapKey, Object key) {
        redissonClient.getMap(mapKey).fastRemove(key);
    }

    public <T> T mapRemove(String mapKey, Object key) {
        return (T) redissonClient.getMap(mapKey).remove(key);
    }

    public void mapClear(String mapKey) {
        redissonClient.getMap(mapKey).clear();
    }

    public <T> T mapGet(String mapKey, Object key) {
        return (T) redissonClient.getMap(mapKey).get(key);
    }

    public RMap getMap(String mapKey) {
        return redissonClient.getMap(mapKey);
    }

    public void mapPutAll(String mapKey, Map value) {
        redissonClient.getMap(mapKey).putAll(value);
    }

    /************************* MAP CACHE BEGIN *****************************/

    public void setCacheMapMaxSize(String mapKey, int size) {
        redissonClient.getMapCache(mapKey).setMaxSize(size);
    }

    public <K, V> void cacheMapPut(String mapKey, K key, V value) {
        redissonClient.getMapCache(mapKey).fastPut(key, value);
    }

    public <K, V> void cacheMapPut(String mapKey, K key, V value, int time, TimeUnit timeUnit) {
        redissonClient.getMapCache(mapKey).fastPut(key, value, time, timeUnit);
    }

    public void cacheMapFastRemove(String mapKey, Object key) {
        redissonClient.getMapCache(mapKey).fastRemove(key);
    }

    public <T> T cacheMapRemove(String mapKey, Object key) {
        return (T) redissonClient.getMapCache(mapKey).remove(key);
    }

    public void cacheMapClear(String mapKey) {
        redissonClient.getMapCache(mapKey).clear();
    }

    public <T> T cacheMapGet(String mapKey, Object key) {
        return (T) redissonClient.getMapCache(mapKey).get(key);
    }

    public void cacheMapPutAll(String mapKey, Map value) {
        redissonClient.getMapCache(mapKey).putAll(value);
    }

    /************************* MultiMap BEGIN *****************************/

    public <K, V> void mulMapPut(String mapKey, K key, V value) {
        redissonClient.getListMultimap(mapKey).put(key, value);
    }

    public Set mulMapRemove(String mapKey, Object key) {
        return redissonClient.getSetMultimap(mapKey).removeAll(key);
    }

    public void mulMapClear(String mapKey) {
        redissonClient.getSetMultimap(mapKey).clear();
    }

    public RSet MulMapGet(String mapKey, Object key) {
        return redissonClient.getSetMultimap(mapKey).get(key);
    }

    /************************* MultiListMap BEGIN *****************************/

    public <K, V> void mulListPut(String mapKey, K key, V value) {
        redissonClient.getListMultimap(mapKey).put(key, value);
    }

    public List mulListRemove(String mapKey, Object key) {
        return redissonClient.getListMultimap(mapKey).removeAll(key);
    }

    public void mulListClear(String mapKey) {
        redissonClient.getListMultimap(mapKey).clear();
    }

    public RList getMulList(String mapKey, Object key) {
        return redissonClient.getListMultimap(mapKey).get(key);
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


    public void initIndex() {
        RAtomicLong rAtomicLong = redissonClient.getAtomicLong("increment");
        long index = rAtomicLong.get();
        if (index == 0) {
            rAtomicLong.set(LOCAL_SOCKET_RANGE);
        }
    }

    public int getNextIndex() {
        return (int) redissonClient.getAtomicLong("increment").incrementAndGet();
    }
}
