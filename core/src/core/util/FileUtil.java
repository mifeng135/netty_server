package core.util;


import core.msg.ClientExceptionMsg;

import java.io.*;
import java.nio.channels.FileChannel;
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

    public static boolean makeDirectory(String path) {
        File file = new File(path);
        if (!file.isFile()) {
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                return parent.mkdirs();
            }
        }
        checkCopyFile(file);
        return true;
    }

    /**
     * 检测是否要生成文件 超过10M 生成新的文件
     *
     * @param file
     */
    public static void checkCopyFile(File file) {
        long size = file.length();
        int mSize = (int) (size / 1024 / 1024);
        if (mSize > 10) {
            String parentPath = file.getParent();
            String destPath = parentPath + "/" + System.currentTimeMillis() + ".txt";
            try {
                copyFile(file.getPath(), destPath);
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write("");
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeClientError(List<ClientExceptionMsg> errorList) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < errorList.size(); i++) {
            builder.append(TimeUtil.stampToDate(errorList.get(i).getTime()));
            builder.append(" ");
            builder.append(errorList.get(i).getException());
            builder.append("\n");
        }
        writeStringToFile("client_error.txt", "client_error", builder.toString());
    }

    /**
     * 将内容写入文件
     *
     * @param fileName
     * @param filePath
     * @param content
     */
    public static void writeStringToFile(String fileName, String filePath, String content) {
        String path = System.getProperty("user.dir") + "/" + filePath + "/" + fileName;
        if (!makeDirectory(path)) {
            return;
        }
        FileWriter fw = null;
        try {
            fw = new FileWriter(path, true);
            fw.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fw != null) {
                try {
                    fw.flush();
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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

    /**
     * 转驼峰命名
     *
     * @param str
     * @return
     * @throws IOException
     */
    public static String unescapeJava(String str) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char code = str.charAt(i);
            switch (code) {
                case '\t':
                    break;
                default:
                    builder.append(code);
            }
        }
        return builder.toString().trim();
    }


    /**
     * 拷贝文件
     *
     * @param sourcePath
     * @param destPath
     * @throws IOException
     */
    private static void copyFile(String sourcePath, String destPath) throws IOException {
        File source = new File(sourcePath);
        File dest = new File(destPath);
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            inputChannel.close();
            outputChannel.close();
        }
    }
}
