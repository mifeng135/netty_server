package core.annotation;

import com.esotericsoftware.reflectasm.ConstructorAccess;
import com.esotericsoftware.reflectasm.MethodAccess;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TableAnnotation {


    private ConfigurationBuilder configurationBuilder;
    private Reflections reflections;
    private String packName = "";
    private Map<String, ConstructorAccess> tableMap = new HashMap<>();

    private static class DefaultInstance {
        static final TableAnnotation INSTANCE = new TableAnnotation();
    }

    public static TableAnnotation getInstance() {
        return TableAnnotation.DefaultInstance.INSTANCE;
    }

    private TableAnnotation() {

    }

    public void init(String packName) {
        initScan(packName);
    }

    private void initScan(String packName) {
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
        Set<Class<?>> classSet = reflections.getTypesAnnotatedWith(Table.class);
        for (Class clazz : classSet) {
            ConstructorAccess<?> classAccess = ConstructorAccess.get(clazz);
            Table table = (Table) clazz.getAnnotation(Table.class);
            String tableName = table.name();
            tableMap.put(tableName, classAccess);
        }
    }

    public ConstructorAccess getTableMap(String key) {
        return tableMap.get(key);
    }
}
