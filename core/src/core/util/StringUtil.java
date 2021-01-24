package core.util;

public class StringUtil {

    public static String trim(String str) {
        return str.trim();
    }

    public static boolean isEmpty(String str) {
        str = str.trim();
        if (str.length() <= 0) {
            return true;
        }
        return false;
    }
}
