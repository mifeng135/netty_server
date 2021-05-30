package core.annotation;


import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RedisA {
    private final Map<String, RedisInfo> classMap = new HashMap<>();

    private ConfigurationBuilder configurationBuilder;
    private Reflections reflections;
    private String packName = "";

    private static class DefaultInstance {
        static final RedisA INSTANCE = new RedisA();
    }

    public static RedisA getInstance() {
        return RedisA.DefaultInstance.INSTANCE;
    }

    private RedisA() {
        initReflection();
        scanClassMap();
    }

    /**
     * init reflection
     */
    private void initReflection() {
        configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.forPackages("bean.login");
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
            Field[] fields = cl.getDeclaredFields();
            RedisInfo redisInfo = new RedisInfo();
            for (Field field : fields) {
                String name = field.getName();
                RedisId redisId = field.getAnnotation(RedisId.class);
                if (redisId != null) {
                    redisInfo.setRedisId(name);
                    break;
                }
            }
            Redis cmd = (Redis) cl.getAnnotation(Redis.class);
            redisInfo.setRedisKey(cmd.redisKey());
            redisInfo.setCls(cl);
            try {
                classMap.put(cmd.name(), redisInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public RedisInfo getRedisInfo(String tableName) {
        return classMap.get(tableName);
    }
}
