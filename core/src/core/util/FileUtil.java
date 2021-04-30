package core.util;


import java.io.*;

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


    public static String getConfigFileString(String fileName) {
        try {
            String path = System.getProperty("user.dir") + "/json_config/" + fileName;
            Reader reader = new FileReader(path);
            BufferedReader buffReader = new BufferedReader(reader);
            String line = buffReader.readLine();
            StringBuffer buffer = new StringBuffer();
            while (line != null) {
                buffer.append(line);
                line = buffReader.readLine();
            }
            return buffer.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }
}
