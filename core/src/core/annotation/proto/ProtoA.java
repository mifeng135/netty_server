package core.annotation.proto;


import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.util.*;


public class ProtoA {
    private final Map<String, List<Class>> classMap = new HashMap<>();

    private ConfigurationBuilder configurationBuilder;
    private Reflections reflections;
    private String packName = "";

    private static class DefaultInstance {
        static final ProtoA INSTANCE = new ProtoA();
    }

    public static ProtoA getInstance() {
        return ProtoA.DefaultInstance.INSTANCE;
    }

    private ProtoA() {
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
        Set<Class<?>> classSet = reflections.getTypesAnnotatedWith(Proto.class);
        for (Class cl : classSet) {
            try {

                String 
                classMap.put(redis.name(), redisInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
