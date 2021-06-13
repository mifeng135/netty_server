package core.util;



import java.io.*;
import java.util.ArrayList;
import java.util.List;

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

    public static String getFileString(String fileName, String filePath) {
        try {
            String path = System.getProperty("user.dir") + "/" + filePath + "/" + fileName;
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

    public static List<String> protoFileList(String fileName, String filePath) {
        try {
            List<String> stringList = new ArrayList<>();
            String path = System.getProperty("user.dir") + "/" + filePath + "/" + fileName;
            Reader reader = new FileReader(path);
            BufferedReader buffReader = new BufferedReader(reader);
            String line = buffReader.readLine();
            while (line != null) {
                if (line.length() > 0) {
                    line = unescapeJava(line);
                    stringList.add(line);
                }
                line = buffReader.readLine();
            }
            return stringList;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static String unescapeJava(String str) throws IOException {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char code = str.charAt(i);
            switch (code){
                case '\t':
                    break;
                default:
                    builder.append(code);
            }
        }
        return builder.toString().trim();
    }
}
