package com.linchaolong.excelparser;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import com.linchaolong.excelparser.utils.ExcelUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Excel 导出 lua 文件
 */
public class Excel2Lua {

    public static final String CONFIG_IMPORT = "import";
    public static final String CONFIG_EXPORT = "export";
    public static final String CONFIG_INCLUDE = "include";

    public static final String TYPE_TABLE = "table";
    public static final String TYPE_INTEGER = "int";
    public static final String TYPE_FLOAT = "float";
    public static final String TYPE_STRING = "string";
    public static final String TYPE_BOOLEAN = "bool";

    // 根目录，相关文件路径默认基于配置文件所在目录
    private File rootDir;
    // excel 文件路径
    private File importDir;
    // lua 文件导出路径
    private File exportDir;
    // 包含文件内容标记

    public Excel2Lua(String configPath) {
        initConfig(configPath);
    }

    /**
     * 创建一个 {@link Excel2Lua} 实例
     *
     * @param configPath 配置文件路径
     * @return
     */
    public static Excel2Lua create(String configPath) {
        return new Excel2Lua(configPath);
    }

    /**
     * 遍历excel文件目录，导出lua配置表
     */
    public void run() {
        if (!importDir.exists()) {
            throw new RuntimeException("excel文件目录不存在，请通过" + CONFIG_IMPORT + "字段配置。");
        }

        // 导出所有excel文件
        for (File file : importDir.listFiles()) {
            if (ExcelUtils.isExcel(file.getName())) {
                excel2Lua(file.getPath());
            } else {
                System.err.println(String.format("'%s' is not a excel file!!!", file.getName()));
            }
        }
    }

    /**
     * Excel 导出 lua
     *
     * @param excelPath excel 文件路径
     */
    private void excel2Lua(String excelPath) {

        // 检查文件是否存在
        File excelFile = new File(excelPath);
        if (!excelFile.exists()) {
            throw new RuntimeException(excelPath + " ，文件不存在。");
        }

        // 初始化导出目录
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }
        String excelFileName = excelFile.getName().substring(0, excelFile.getName().lastIndexOf('.'));
        File luaFile = new File(exportDir, excelFileName + ".lua");
        if (luaFile.exists()) {
            luaFile.delete();
        }
        System.out.println("开始导出：" + luaFile.getName());
        // excel表对象
        Workbook workbook = ExcelUtils.workbook(excelPath);

        List<String> typeList = new ArrayList<>();
        // 获取第1页的表格，数据类型
        Sheet sheet = workbook.getSheetAt(0);
        int totalCellNumber = sheet.getRow(0).getLastCellNum();
        for (int i = 0; i < totalCellNumber; i++) {
            Cell cell = sheet.getRow(0).getCell(i);
            if (cell == null) {
                System.out.println("cell is null colume = " + i);
            }
            String type = cell.toString();
            typeList.add(type);
        }
        try {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(luaFile),"UTF-8"));
            out.append("local " + excelFileName + " = {");
            int totalRow = sheet.getLastRowNum();
            Row keyRow = sheet.getRow(1); // 第一行：字段类型描述
            for (int i = 2; i <= totalRow; i++) { // 第二行为数据开始
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;   // 跳过空行
                }
                for (int j = row.getFirstCellNum(); j < totalCellNumber; j++) {
                    Cell cell = row.getCell(j);
                    String cellStr;
                    if ("null".equals(cell + "")) {
                        cellStr = "";
                    } else {
                        cellStr = cell.toString();
                    }
                    String type = typeList.get(j);
                    String cellKey = keyRow.getCell(j).toString();
                    if (j == 0) {
                        out.append("{" + cellKey).append("=" + getValue(cellStr, type) + ",");
                    } else {
                        if (j == totalCellNumber - 1) {
                            out.append(cellKey).append("=").append(getValue(cellStr, type));
                        } else {
                            out.append(cellKey).append("=").append(getValue(cellStr, type)).append(",");
                        }
                    }
                }
                if (i == totalRow) {
                    out.append("}");
                } else {
                    out.append("},");
                }
                out.newLine();
            }
            out.append("}");
            out.newLine();
            out.append("return " + excelFileName);
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(luaFile.getPath() + "，文件导出失败。");
        }
        System.out.println("导出完成：" + luaFile.getName());
    }

    public String getValue(String value, String type) {
        value = value.trim();
        if (type.equals(TYPE_INTEGER)) {
            if (value.length() == 0) {
                return "\"\"";
            }
            if(!isNumber(value)) {
                return value;
            }
            value = Integer.toString((int) Float.parseFloat(value));
        } else if (type.equals(TYPE_STRING)) {
            if(isNumber(value)) {
                return value;
            }
            if (value.length() == 0) {
                return "\"\"";
            }
            value = "\"" + value + "\"";
        } else if (type.equals(TYPE_BOOLEAN)) {
            if (value.length() <= 0) {
                value = "\"\"";
            }
            value = value.toLowerCase();
        }else if (type.equals(TYPE_TABLE)) {
            if(isNumber(value)) {
                return value;
            }
            if (value.length() <= 0) {
                value = "\"\"";
            }
        }else if (type.equals(TYPE_FLOAT)) {
            if (value.length() == 0) {
                return "\"\"";
            }
        }
        return value;
    }

    public static boolean isNumber(String str){
        if (str == null || str.length() == 0) {
            return false;
        }
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            try {
                Double.parseDouble(str);
                return true;
            } catch (NumberFormatException ex) {
                try {
                    Float.parseFloat(str);
                    return true;
                } catch (NumberFormatException exx) {
                    return false;
                }
            }
        }
    }
    /**
     * 初始化配置
     *
     * @param configPath
     */
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
            importDir = new File(rootDir, configMap.get(CONFIG_IMPORT));
            exportDir = new File(rootDir, configMap.get(CONFIG_EXPORT));

            for (Entry<String, String> entry : configMap.entrySet()) {
                System.out.println(entry.getKey() + " = " + entry.getValue());
            }
            System.out.println("配置初始化完成。");
        } catch (FileNotFoundException e) {
            throw new RuntimeException("配置文件" + configPath + "不存在，初始化配置失败。");
        } catch (IOException e) {
            throw new RuntimeException("读取配置文件" + configPath + "失败。");
        }
    }

    public static void main(String[] args) throws Exception {
        //Excel2Lua.create("./Excel2Lua/config.txt").run();
        if (args.length == 2) {
            int type = Integer.parseInt(args[0]);
            String config = args[1];
            if (type == 1) {
                Excel2Lua.create(config).run();
            } else {
                MakeMd5.create(config).run();
            }
            //Excel2Lua.create("./Excel2Lua/config.txt").run();
        }else {
            System.out.println("args error");
        }
        //Excel2Lua.create(args[0]).run(); // 从命令行参数读取文件路径
    }

}
