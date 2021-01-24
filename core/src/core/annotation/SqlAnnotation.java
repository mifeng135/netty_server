package core.annotation;

import core.Constants;
import core.sql.MysqlBean;
import core.sql.SqlConstant;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;


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

    public SqlAnnotation() {
        initMysql();
        initReflection();
        scanSqlMethodMap();
    }

    /**
     * init reflection
     */
    private void initReflection() {
        configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.filterInputsBy(new FilterBuilder().includePackage(Constants.SERVER_PACKAGE_NAME));
        configurationBuilder.addUrls(ClasspathHelper.forPackage(Constants.SERVER_PACKAGE_NAME));
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
        try {
            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-mybatis.xml");
            mSqlSessionTemplate = context.getBean(SqlSessionTemplate.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * select sql
     *
     * @param cmd
     * @param parameter
     * @return sql result
     */
    public <T> T sqlSelectOne(int cmd, Object parameter) {
        Method method = sqlMethodMap.get(cmd);
        if (method == null) {
            return null;
        }
        try {
            int type = method.getAnnotation(SqlCmd.class).sqlType();
            String declaringClass = method.getDeclaringClass().getName() + "." + method.getName();
            if (type == SqlConstant.SELECT_ONE) {
                return mSqlSessionTemplate.selectOne(declaringClass, parameter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> List<T> sqlSelectList(int cmd, Object parameter) {
        Method method = sqlMethodMap.get(cmd);
        if (method == null) {
            return null;
        }
        try {
            int type = method.getAnnotation(SqlCmd.class).sqlType();
            String declaringClass = method.getDeclaringClass().getName() + "." + method.getName();
            if (type == SqlConstant.SELECT_LIST) {
                return mSqlSessionTemplate.selectList(declaringClass, parameter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * commit sql once
     *
     * @param cmd
     * @param parameter
     * @return
     */
    public int executeCommitSql(int cmd, Object parameter) {
        Method method = sqlMethodMap.get(cmd);
        if (method == null) {
            return -1;
        }
        int type = method.getAnnotation(SqlCmd.class).sqlType();
        String declaringClass = method.getDeclaringClass().getName() + "." + method.getName();

        try {
            if (type == SqlConstant.DELETE) {
                return mSqlSessionTemplate.delete(declaringClass, parameter);
            } else if (type == SqlConstant.UPDATE) {
                return mSqlSessionTemplate.update(declaringClass, parameter);
            } else if (type == SqlConstant.INSERT) {
                return mSqlSessionTemplate.insert(declaringClass, parameter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * if data to many
     * you can use this function
     * batch commit will be faster
     *
     * @param batchData
     * @param batchCount
     */
    public void executeCommitSqlBatch(ConcurrentLinkedQueue<MysqlBean> batchData, int batchCount) {
        SqlSession sqlSession = mSqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        try {
            for (int i = 0; i < batchCount; i++) {
                MysqlBean bean = batchData.poll();
                if (bean == null) {
                    break;
                }
                Object parameter = bean.getData();
                Method method = sqlMethodMap.get(bean.getCmd());
                if (method == null) {
                    continue;
                }
                int type = method.getAnnotation(SqlCmd.class).sqlType();
                String declaringClass = method.getDeclaringClass().getName() + "." + method.getName();
                if (type == SqlConstant.DELETE) {
                    sqlSession.delete(declaringClass, parameter);
                } else if (type == SqlConstant.UPDATE) {
                    sqlSession.update(declaringClass, parameter);
                } else if (type == SqlConstant.INSERT) {
                    sqlSession.insert(declaringClass, parameter);
                }
            }
            sqlSession.commit();
        } catch (Exception e) {
            sqlSession.rollback();
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }
}
