package core.annotation.redis;


import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.util.*;


public class RedisA {
    private final Map<String, RedisInfo> classMap = new HashMap<>();

    private ConfigurationBuilder configurationBuilder;
    private Reflections reflections;
    private String packName = "";

    public RedisA(String packName) {
        this.packName = packName;
        initReflection();
        scanClassMap();
    }

    /**
     * init reflection
     */
    private void initReflection() {
        configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.forPackages(packName);
        configurationBuilder.setScanners(new SubTypesScanner(false), new TypeAnnotationsScanner(), new MethodAnnotationsScanner());
        configurationBuilder.filterInputsBy(new FilterBuilder().includePackage(packName));
        reflections = new Reflections(configurationBuilder);
    }

    /**
     * scan all Ctrl method
     * add all class instance to class map
     */
    private void scanClassMap() {
        Set<Class<?>> classSet = reflections.getTypesAnnotatedWith(Redis.class);
        for (Class cl : classSet) {
            RedisInfo redisInfo = new RedisInfo();
            Redis redis = (Redis) cl.getAnnotation(Redis.class);
            redisInfo.setCls(cl);
            redisInfo.setTableName(redis.name());
            redisInfo.setIncrName(redis.IncrName());
            redisInfo.setDbName(redis.dbName());
            redisInfo.setImmediately(redis.immediately());
            redisInfo.setIncr(redis.incrId());
            redisInfo.setDelete(redis.delete());
            try {
                classMap.put(redis.name(), redisInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public Map<String, RedisInfo> getClassMap() {
        return classMap;
    }
}
