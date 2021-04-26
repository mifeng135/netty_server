package com.linchaolong.excelparser;

import com.linchaolong.excelparser.utils.ExcelUtils;
import sun.dc.pr.PRError;

import java.io.*;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

public class MakeMd5 {

    public static final String CONFIG_FILE_PATH_DIR = "filePathDir";
    // 根目录
    private File rootDir;
    // excel 文件路径
    private File filePathDir;

    public static MakeMd5 create(String configPath) {
        return new MakeMd5(configPath);
    }

    public MakeMd5(String configPath) {
        initConfig(configPath);
    }

    private void initConfig(String configPath) {
        File file = new File(configPath);
        if (!file.exists()) {
            throw new RuntimeException("配置文件 '" + file.getAbsolutePath() + "' 不存在，初始化配置失败。");
        }

        rootDir = file.getParentFile();
        Map<String, String> configMap = new HashMap<>();
        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            System.out.println("初始化配置。");
            String line;
            while ((line = in.readLine()) != null) {
                if (line.contains("=")) {
                    String kv[] = line.split("=");
                    configMap.put(kv[0].trim(), kv[1].trim());
                }
            }
            filePathDir = new File(rootDir, configMap.get(CONFIG_FILE_PATH_DIR));
            for (Map.Entry<String, String> entry : configMap.entrySet()) {
                System.out.println(entry.getKey() + " = " + entry.getValue());
            }
            System.out.println("配置初始化完成。");
        } catch (FileNotFoundException e) {
            throw new RuntimeException("配置文件" + configPath + "不存在，初始化配置失败。");
        } catch (IOException e) {
            throw new RuntimeException("读取配置文件" + configPath + "失败。");
        }
    }


    /**
     *
     */
    public void run() throws Exception {
        if (!filePathDir.exists()) {
            throw new RuntimeException("excel文件目录不存在，请通过" + CONFIG_FILE_PATH_DIR + "字段配置。");
        }
        File updateList = new File(filePathDir, "updatelist.txt");
        if (updateList.exists()) {
            updateList.delete();
        }
        BufferedWriter out = new BufferedWriter(new FileWriter(updateList));
        String path = "src/app/include/";
        int index = 0;
        File[] listFiles = filePathDir.listFiles(pathname -> {
            if (!ExcelUtils.isLua(pathname.getName())) {
                return false;
            }
            if (ExcelUtils.filterLua(pathname.getName())) {
                return false;
            }
            return true;
        });
        long fileLen = listFiles.length;
        for (File file : listFiles) {
            index++;
            if (!ExcelUtils.isLua(file.getName())) {
                continue;
            }
            if (ExcelUtils.filterLua(file.getName())) {
                continue;
            }
            String fullPath = path + file.getName();
            String md5 = makeMd5(file);
            long length = file.length();
            if (index == fileLen) {
                out.append(fullPath).append("*").append(md5).append("*").append(Long.toString(length));
            } else {
                out.append(fullPath).append("*").append(md5).append("*").append(length + "|");
            }
        }
        out.close();
        System.out.println("md5 文件 生成 成功");
        writeVerLuaFile();
    }

    private void writeVerLuaFile() throws Exception {
        File verLua = new File(filePathDir, "ver.lua");
        int version = 1;
        if (verLua.exists()) {
            version = getClientVer(verLua);
            verLua.delete();
            writeVersionFile(verLua,version + 1);
        } else {
            writeVersionFile(verLua,version);
        }
    }

    private void writeVersionFile(File verLua,int version) {
        String str = "local clientVer = " + version;
        String returnStr = "return clientVer";
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(verLua));
            out.append(str);
            out.newLine();
            out.append(returnStr);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getClientVer(File verLua) throws Exception {
        int version = 1;
        InputStreamReader reader = new InputStreamReader(new FileInputStream(verLua), "UTF-8");
        BufferedReader bufferedReader = new BufferedReader(reader);
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            if (line.contains("=")) {
                String kv[] = line.split("=");
                version = Integer.parseInt(kv[1].trim());
            }
        }
        return version;
    }
    public String makeMd5(File file) {
        String value = "";
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(16).toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return value;
    }

    public static void main(String[] args) throws Exception {

        String ds = " 111  11 ";
        ds = ds.trim();

        System.out.println(ds);
        //MakeMd5.create("./Excel2Lua/pathFileConfig.txt").run();
        //MakeMd5.create(args[0]).run();
        //Excel2Lua.create(args[0]).run(); // 从命令行参数读取文件路径
    }
}
