package core.annotation;

import com.esotericsoftware.reflectasm.ConstructorAccess;
import com.esotericsoftware.reflectasm.MethodAccess;
import core.Constants;
import core.msg.TransferMsg;
import io.netty.channel.ChannelHandlerContext;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2020/5/28.
 */
public class CtrlAnnotation {


    private static Logger logger = LoggerFactory.getLogger(CtrlAnnotation.class);

    private final Map<Integer, Method> methodMap = new HashMap<>();
    private final Map<String, Object> classMap = new HashMap<>();
    private final Map<String, MethodAccess> methodAccessMap = new HashMap<>();

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
        configurationBuilder.forPackages(Constants.SERVER_PACKAGE_NAME);
        configurationBuilder.setScanners(new SubTypesScanner(false), new TypeAnnotationsScanner(), new MethodAnnotationsScanner());
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

    public void invokeMethod(TransferMsg transferMsg) {
        int msgId = transferMsg.getMsgId();
        Method method = methodMap.get(msgId);
        if (method == null) {
            logger.info("can not find msgId = {}", msgId);
            return;
        }
        String declaringClassName = method.getDeclaringClass().getName();
        Object oc = classMap.get(declaringClassName);
        MethodAccess methodAccess = methodAccessMap.get(declaringClassName);
        String methodName = method.getName();
        try {
            methodAccess.invoke(oc, methodName, transferMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void invokeMethod(TransferMsg transferMsg, ChannelHandlerContext context) {
        int msgId = transferMsg.getMsgId();
        Method method = methodMap.get(msgId);
        if (method == null) {
            logger.info("can not find msgId = {}", msgId);
            return;
        }
        String declaringClassName = method.getDeclaringClass().getName();
        Object oc = classMap.get(declaringClassName);
        MethodAccess methodAccess = methodAccessMap.get(declaringClassName);
        String methodName = method.getName();
        try {
            methodAccess.invoke(oc, methodName, transferMsg, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}