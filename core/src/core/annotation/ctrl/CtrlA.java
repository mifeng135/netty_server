package core.annotation.ctrl;

import com.esotericsoftware.reflectasm.ConstructorAccess;
import com.esotericsoftware.reflectasm.MethodAccess;
import core.exception.BaseExceptionHandler;
import core.exception.ExceptionHandler;
import core.group.EventThreadGroup;
import core.msg.TransferMsg;
import core.util.Util;
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
 * Created by Administrator on 2020/5/28.
 * ctrl annotation
 */
public class CtrlA {


    private static Logger logger = LoggerFactory.getLogger(CtrlA.class);


    private final Map<Integer, Method> methodMap = new HashMap<>();
    private final Map<String, Method> httpMethodMap = new HashMap<>();
    private final Map<String, Object> classMap = new HashMap<>();
    private final Map<String, MethodAccess> methodAccessMap = new HashMap<>();
    private final Map<Integer, CtrlCmd> ctrlCmdMap = new HashMap<>();


    private final List<Integer> msgList = new ArrayList<>();

    private ConfigurationBuilder configurationBuilder;
    private Reflections reflections;
    private String packName = "";
    private ExceptionHandler exceptionHandler = new BaseExceptionHandler();

    private static class DefaultInstance {
        static final CtrlA INSTANCE = new CtrlA();
    }

    public static CtrlA getInstance() {
        return DefaultInstance.INSTANCE;
    }

    private CtrlA() {

    }

    public void init(String packName) {
        initScan(packName);
    }

    public void init(String packName, ExceptionHandler handler) {
        initScan(packName);
        exceptionHandler = handler;
    }

    public void init(Class cls, ExceptionHandler handler) {
        initScan(Util.getPackageName(cls));
        exceptionHandler = handler;
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
        Set<Method> methodSet = reflections.getMethodsAnnotatedWith(CtrlCmd.class);
        for (Method method : methodSet) {
            CtrlCmd ctrlCmd = method.getAnnotation(CtrlCmd.class);
            int cmd = ctrlCmd.cmd();
            String httpCmd = ctrlCmd.httpCmd();
            if (httpCmd.length() > 0) {
                httpMethodMap.put(httpCmd, method);
            }
            methodMap.put(cmd, method);
            msgList.add(cmd);
            ctrlCmdMap.put(cmd, ctrlCmd);
        }
    }

    /**
     * scan all Ctrl method
     * add all class instance to class map
     */
    private void scanClassMap() {
        Set<Class<?>> classSet = reflections.getTypesAnnotatedWith(Ctrl.class);
        for (Class cl : classSet) {
            String mm = cl.getPackage().getName();
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

    public Map<Integer, Method> getMethodMap() {
        return methodMap;
    }

    public boolean containMethod(int msgId) {
        return methodMap.containsKey(msgId);
    }

    public Map<String, Object> getClassMap() {
        return classMap;
    }

    public Map<String, MethodAccess> getMethodAccessMap() {
        return methodAccessMap;
    }

    public List<Integer> getMsgList() {
        return msgList;
    }

    public Map<Integer, CtrlCmd> getCtrlCmdMap() {
        return ctrlCmdMap;
    }

    public void invokeHttpMethod(TransferMsg msg) {
        String httpUrl = msg.getHttpUrl();
        Method method = httpMethodMap.get(httpUrl);
        if (method == null) {
            logger.info("can not find msgId = {}", httpUrl);
            return;
        }
        String declaringClassName = method.getDeclaringClass().getName();
        Object oc = classMap.get(declaringClassName);
        MethodAccess methodAccess = methodAccessMap.get(declaringClassName);
        String methodName = method.getName();
        try {
            methodAccess.invoke(oc, methodName, msg);
        } catch (Throwable err) {
            if (exceptionHandler != null) {
                exceptionHandler.onException(err, msg);
            } else {
                err.printStackTrace();
            }
        }
    }

    public void invokeMethod(TransferMsg msg) {
        int msgId = msg.getHeaderProto().getMsgId();
        Method method = methodMap.get(msgId);
        if (method == null) {
            if (msgId > 1000) {
                logger.info("can not find msgId = {}", msgId);
            }
            return;
        }
        String declaringClassName = method.getDeclaringClass().getName();
        Object oc = classMap.get(declaringClassName);
        MethodAccess methodAccess = methodAccessMap.get(declaringClassName);
        String methodName = method.getName();
        try {
            methodAccess.invoke(oc, methodName, msg);
        } catch (Throwable err) {
            exceptionHandler.onException(err, msg);
        }
    }
}
