package com.game.db.core.annotation;

import com.game.db.serverConfig.ServerConfig;
import com.game.db.core.sql.SqlConstant;
import org.mybatis.spring.SqlSessionTemplate;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class SqlAnnotation {

    public final Map<Integer, Method> sqlMethodMap = new HashMap<>();

    private ConfigurationBuilder configurationBuilder;
    private Reflections reflections;
    private SqlSessionTemplate mSqlSessionTemplate;

    private static class DefaultInstance {
        static final SqlAnnotation INSTANCE = new SqlAnnotation();
    }

    public static SqlAnnotation getInstance() {
        return DefaultInstance.INSTANCE;
    }

    private SqlAnnotation() {
        initMysql();
        initReflection();
        scanSqlMethodMap();
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

    private void scanSqlMethodMap() {
        Set<Method> methodSet = reflections.getMethodsAnnotatedWith(SqlCmd.class);
        for (Method method : methodSet) {
            int cmd = method.getAnnotation(SqlCmd.class).sqlCmd();
            sqlMethodMap.put(cmd, method);
        }
    }

    private void initMysql() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-mybatis.xml");
        mSqlSessionTemplate = context.getBean(SqlSessionTemplate.class);
    }

    public Object executeSelectSql(int cmd, Object parameter) {
        Method method = sqlMethodMap.get(cmd);
        if (method == null) {
            return null;
        }
        int type = method.getAnnotation(SqlCmd.class).sqlType();
        String declaringClass = method.getDeclaringClass().getName() + "." + method.getName();
        if (type == SqlConstant.SELECT_ONE) {
            return mSqlSessionTemplate.selectOne(declaringClass, parameter);
        } else if (type == SqlConstant.SELECT_LIST) {
            return mSqlSessionTemplate.selectList(declaringClass, parameter);
        }
        return null;
    }

    public int executeCommitSql(int cmd, Object parameter) {
        Method method = sqlMethodMap.get(cmd);
        if (method == null) {
            return -1;
        }
        int type = method.getAnnotation(SqlCmd.class).sqlType();
        String declaringClass = method.getDeclaringClass().getName() + "." + method.getName();
        if (type == SqlConstant.DELETE) {
            return mSqlSessionTemplate.delete(declaringClass, parameter);
        }else if (type == SqlConstant.UPDATE) {
            return mSqlSessionTemplate.update(declaringClass, parameter);
        }else if (type == SqlConstant.INSERT) {
            return mSqlSessionTemplate.insert(declaringClass, parameter);
        }
        return -1;
    }

}
