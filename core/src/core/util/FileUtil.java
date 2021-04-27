package core.util;

import org.nutz.lang.Files;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class FileUtil {

    public static String getFilePath(String fileName) {
        return System.getProperty("user.dir") + "/server_config/" + fileName;
    }

    public static InputStream getInputStream(String fileName) {
        String path = getFilePath(fileName);
        try {
            return new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
