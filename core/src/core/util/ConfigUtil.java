package core.util;

import java.io.*;
import java.util.Properties;

public class ConfigUtil {

    private static Properties properties = new Properties();

    public static void loadFile(String fileName) {
        InputStream inputStream = ConfigUtil.class.getClassLoader().getResourceAsStream(fileName);
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getString(String key) {
        String value = properties.getProperty(key);
        if (value == null || value.isEmpty()) {
            return "";
        }
        return value;
    }

    public static int getInt(String key, int defaultValue) {
        String value = properties.getProperty(key);
        if (value == null || value.isEmpty()) {
            return defaultValue;
        }
        return Integer.valueOf(value);
    }

    public static int getInt(String key) {
        String value = properties.getProperty(key);
        if (value == null || value.isEmpty()) {
            return 0;
        }
        return Integer.valueOf(value);
    }

    public static Boolean getBoolean(String key) {
        String value = properties.getProperty(key);
        if (value == null || value.isEmpty()) {
            return false;
        }
        return value.compareToIgnoreCase("true") == 0;
    }
}
