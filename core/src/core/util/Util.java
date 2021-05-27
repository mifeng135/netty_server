package core.util;

public class Util {

    public static int getRunProcessor() {
        return Runtime.getRuntime().availableProcessors();
    }

    public static String getPackageName(Class cls) {
        return cls.getPackage().getName();
    }

}
