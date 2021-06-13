package core.util;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Util {

    public static int getRunProcessor() {
        return Runtime.getRuntime().availableProcessors();
    }

    public static String getPackageName(Class cls) {
        return cls.getPackage().getName();
    }

    public static List mapToList(Map map) {
        if (map == null) {
            return null;
        }
        return new ArrayList(map.values());
    }

    public static Map listToMap(List list, Function function) {
        if (list == null) {
            return null;
        }
        return (Map) list.stream().collect(Collectors.toMap(function, (p) -> p));
    }
}
