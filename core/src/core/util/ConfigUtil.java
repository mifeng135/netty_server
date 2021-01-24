package core.util;

import java.io.*;
import java.util.Properties;

public class ConfigUtil {

    private static Properties properties = new Properties();

    public static void loadProperti(String fileName) {
        InputStream inss = ConfigUtil.class.getClassLoader().getResourceAsStream(fileName);
        try {
            properties.load(inss);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
