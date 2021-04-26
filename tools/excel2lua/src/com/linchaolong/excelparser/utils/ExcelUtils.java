package com.linchaolong.excelparser.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

/**
 * Excel 工具类
 * <p>
 * Created by linchaolong on 2016/12/12.
 */
public class ExcelUtils {

    public static final int CELL_VALUE_ERROR = -1;
    public static final String SUFFIX_XLS = ".xls";
    public static final String SUFFIX_XLSX = ".xlsx";

    public static final String SUFFIX_LUA = ".lua";
    public static final String FILE_NAME = "ver";

    /**
     * 是否是excel文件
     *
     * @param name
     * @return
     */
    public static boolean isExcel(String name) {
        if (null != name) {
            String fileType = name
                    .substring(name.lastIndexOf("."))
                    .trim().toLowerCase();
            return SUFFIX_XLS.equals(fileType) || SUFFIX_XLSX.equals(fileType);
        }
        return false;
    }

    public static boolean isLua(String name) {
        if (null != name) {
            String fileType = name
                    .substring(name.lastIndexOf("."))
                    .trim().toLowerCase();
            return SUFFIX_LUA.equals(fileType);
        }
        return false;
    }
    public static boolean filterLua(String name) {
        if (null != name) {
            String fileName = name
                    .substring(0,name.indexOf("."))
                    .trim().toLowerCase();
            return fileName.equals(FILE_NAME);
        }
        return false;
    }

    /**
     * 根据文件名获取excel对象
     *
     * @param excel
     * @return
     */
    public static Workbook workbook(String excel) {
        return workbook(new File(excel));
    }

    /**
     * 根据文件名获取excel对象
     *
     * @param excel
     * @return
     */
    public static Workbook workbook(File excel) {
        Workbook workbook = null;
        if (null != excel) {
            String fileType = getFileSuffix(excel.getName());
            try {
                FileInputStream fileStream = new FileInputStream(excel);
                if (".xls".equals(fileType)) {
                    workbook = new HSSFWorkbook(fileStream);
                } else if (".xlsx".equals(fileType)) {
                    workbook = new XSSFWorkbook(fileStream);
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(excel + "，文件找不到。");
            } catch (IOException e) {
                throw new RuntimeException(excel + "，文件读取失败。");
            }
        }
        return workbook;
    }


    /**
     * 获取文件后缀
     *
     * @param filename
     * @return
     */
    private static String getFileSuffix(String filename) {
        return filename.substring(filename.lastIndexOf("."), filename.length())
                .trim().toLowerCase();
    }
}
