package com.game.db.core.annotation;

import com.game.db.serverConfig.ServerConfig;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2020/5/28.
 */
public class CtrlAnnotation {

    private final Map<Integer, Method> methodMap = new HashMap<Integer, Method>();
    private final Map<String, Object> classMap = new HashMap<String, Object>();

    private ConfigurationBuilder configurationBuilder;
    private Reflections reflections;


    private static class DefaultInstance {
        static final CtrlAnnotation INSTANCE = new CtrlAnnotation();
    }

    public static CtrlAnnotation getInstance() {
        return DefaultInstance.INSTANCE;
    }

    private CtrlAnnotation() {
        initReflection();
        scanMethodMap();
        scanClassMap();
    }

    /**
     * init reflection
     */
    private void initReflection() {
        configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.filterInputsBy(new FilterBuilder().includePackage(ServerConfig.SERVER_PACKAGE));
        configurationBuilder.addUrls(ClasspathHelper.forPackage(ServerConfig.SERVER_PACKAGE));
        configurationBuilder.setScanners(new TypeAnnotationsScanner(), new SubTypesScanner(), new MethodAnnotationsScanner());
        reflections = new Reflections(configurationBuilder);
    }

    /**
     * scan all CtrlCmd method
     * add all method to method map
     */
    private void scanMethodMap() {
        Set<Method> methodSet = reflections.getMethodsAnnotatedWith(CtrlCmd.class);
        for (Method method : methodSet) {
            int cmd = method.getAnnotation(CtrlCmd.class).cmd();
            methodMap.put(cmd, method);
        }
    }

    /**
     * scan all Ctrl method
     * add all class instance to class map
     */
    private void scanClassMap() {
        Set<Class<?>> classSet = reflections.getTypesAnnotatedWith(Ctrl.class);
        for (Class cl : classSet) {
            String name = cl.getName();
            try {
                Object oc = Class.forName(name).newInstance();
                classMap.put(name, oc);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * call method
     *
     * @param cmd
     * @param data receive data
     */
    public void invokeMethod(int cmd, int id, byte[] data) {
        Method method = methodMap.get(cmd);
        if (method == null) {
            return;
        }
        String declaringClassName = method.getDeclaringClass().getName();
        Object oc = classMap.get(declaringClassName);
        try {
            method.invoke(oc, id, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
