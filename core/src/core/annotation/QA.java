package core.annotation;

import com.esotericsoftware.reflectasm.ConstructorAccess;
import com.esotericsoftware.reflectasm.MethodAccess;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.*;


/**
 * query annotation
 */
public class QA {

    private static Logger logger = LoggerFactory.getLogger(CA.class);

    private final Map<Integer, Method> methodMap = new HashMap<>();
    private final Map<String, Object> classMap = new HashMap<>();
    private final Map<String, MethodAccess> methodAccessMap = new HashMap<>();

    private ConfigurationBuilder configurationBuilder;
    private Reflections reflections;
    private String packName = "";

    private static class DefaultInstance {
        static final QA INSTANCE = new QA();
    }

    public static QA getInstance() {
        return QA.DefaultInstance.INSTANCE;
    }

    private QA() {

    }

    public void init(String packName) {
        initScan(packName);
    }

    private void initScan(String packName) {
        this.packName = packName;
        initReflection();
        scanMethodMap();
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
     * scan all CtrlCmd method
     * add all method to method map
     */
    private void scanMethodMap() {
        Set<Method> methodSet = reflections.getMethodsAnnotatedWith(Query.class);
        for (Method method : methodSet) {
            int cmd = method.getAnnotation(Query.class).cmd();
            methodMap.put(cmd, method);
        }
    }

    /**
     * scan all Ctrl method
     * add all class instance to class map
     */
    private void scanClassMap() {
        Set<Class<?>> classSet = reflections.getTypesAnnotatedWith(QueryCtrl.class);
        for (Class cl : classSet) {
            MethodAccess access = MethodAccess.get(cl);
            ConstructorAccess<?> classAccess = ConstructorAccess.get(cl);
            String name = cl.getName();
            try {
                Object oc = classAccess.newInstance();
                classMap.put(name, oc);
                methodAccessMap.put(name, access);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public <T> T invokeQuery(int cmd, Object... args) {
        Method method = methodMap.get(cmd);
        if (method == null) {
            return null;
        }

        T ret = null;
        String declaringClassName = method.getDeclaringClass().getName();
        Object oc = classMap.get(declaringClassName);
        MethodAccess methodAccess = methodAccessMap.get(declaringClassName);
        String methodName = method.getName();
        try {
            ret = (T) methodAccess.invoke(oc, methodName, args);
        } catch (Throwable err) {
            err.printStackTrace();
        }
        return ret;
    }

    public <T> T invokeQuery(int cmd, Map args) {
        Method method = methodMap.get(cmd);
        if (method == null) {
            return null;
        }
        T ret = null;
        String declaringClassName = method.getDeclaringClass().getName();
        Object oc = classMap.get(declaringClassName);
        MethodAccess methodAccess = methodAccessMap.get(declaringClassName);
        String methodName = method.getName();
        try {
            ret = (T) methodAccess.invoke(oc, methodName, args);
        } catch (Throwable err) {
            err.printStackTrace();
        }
        return ret;
    }


    public <T> T invokeQuery(int cmd, List args) {
        Method method = methodMap.get(cmd);
        if (method == null) {
            return null;
        }
        T ret = null;
        String declaringClassName = method.getDeclaringClass().getName();
        Object oc = classMap.get(declaringClassName);
        MethodAccess methodAccess = methodAccessMap.get(declaringClassName);
        String methodName = method.getName();
        try {
            ret = (T) methodAccess.invoke(oc, methodName, args);
        } catch (Throwable err) {
            err.printStackTrace();
        }
        return ret;
    }
}
