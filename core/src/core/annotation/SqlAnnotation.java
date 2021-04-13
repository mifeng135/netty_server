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

import static core.Constants.SQL_MASTER;


public class SqlAnnotation {

    public final Map<Integer, Method> sqlMethodMap = new HashMap<>();

    private ConfigurationBuilder configurationBuilder;
    private Reflections reflections;
    private Map<Integer, SqlSessionTemplate> sqlSessionTemplateMap = new HashMap<>();

    private static class DefaultInstance {
        static final SqlAnnotation INSTANCE = new SqlAnnotation();
    }

    public static SqlAnnotation getInstance() {
        return DefaultInstance.INSTANCE;
    }

    public SqlAnnotation() {
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

    public void intiSqlWithKey(int key, String config) {
        try {
            String path = "classpath:" + config;
            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(path);
            SqlSessionTemplate sqlSessionTemplate = context.getBean(SqlSessionTemplate.class);
            sqlSessionTemplateMap.put(key, sqlSessionTemplate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private SqlSessionTemplate getSqlSessionTemplate(int key) {
        return sqlSessionTemplateMap.get(key);
    }

    /**
     * select sql
     *
     * @param cmd
     * @param parameter
     * @return sql result one item or null
     */
    public <T> T sqlSelectOne(int cmd, Object parameter) {
        Method method = sqlMethodMap.get(cmd);
        if (method == null) {
            return null;
        }
        try {
            int type = method.getAnnotation(SqlCmd.class).sqlType();
            String declaringClass = method.getDeclaringClass().getName() + "." + method.getName();
            int key = method.getAnnotation(SqlCmd.class).sqlKey();
            if (type == SqlConstant.SELECT_ONE) {
                return getSqlSessionTemplate(key).selectOne(declaringClass, parameter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /***
     * select sql
     * @param cmd
     * @param <T>
     * @return result list or null
     */
    public <T> List<T> sqlSelectList(int cmd) {
        return sqlSelectOne(cmd, null);
    }
    /***
     * select sql
     * @param cmd
     * @param parameter
     * @param <T>
     * @return result list or null
     */
    public <T> List<T> sqlSelectList(int cmd, Object parameter) {
        return sqlSelectOne(cmd, parameter);
    }

    private <T> List<T> sqlSelect(int cmd, Object parameter) {
        Method method = sqlMethodMap.get(cmd);
        if (method == null) {
            return null;
        }
        try {
            int type = method.getAnnotation(SqlCmd.class).sqlType();
            String declaringClass = method.getDeclaringClass().getName() + "." + method.getName();
            int key = method.getAnnotation(SqlCmd.class).sqlKey();
            if (type == SqlConstant.SELECT_LIST) {
                if (parameter == null) {
                    return getSqlSessionTemplate(key).selectList(declaringClass);
                } else {
                    return getSqlSessionTemplate(key).selectList(declaringClass, parameter);
                }
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
     * @return resut 1 success
     */
    public int executeCommitSql(int cmd, Object parameter) {
        Method method = sqlMethodMap.get(cmd);
        if (method == null) {
            return -1;
        }
        int type = method.getAnnotation(SqlCmd.class).sqlType();
        String declaringClass = method.getDeclaringClass().getName() + "." + method.getName();
        int key = method.getAnnotation(SqlCmd.class).sqlKey();
        try {
            if (type == SqlConstant.DELETE) {
                return getSqlSessionTemplate(key).delete(declaringClass, parameter);
            } else if (type == SqlConstant.UPDATE) {
                return getSqlSessionTemplate(key).update(declaringClass, parameter);
            } else if (type == SqlConstant.INSERT) {
                return getSqlSessionTemplate(key).insert(declaringClass, parameter);
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
        SqlSession sqlSession = getSqlSessionTemplate(SQL_MASTER).getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
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
