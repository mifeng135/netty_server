package com.game.logic;


import com.esotericsoftware.reflectasm.ConstructorAccess;
import core.BaseTable;
import core.annotation.TableAnnotation;
import core.util.FileUtil;
import org.nutz.json.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class LoadConfig {

    private static Logger logger = LoggerFactory.getLogger(LoadConfig.class);

    private String rootDirPath = System.getProperty("user.dir") + "/json_config";

    public void load() {
        File file = new File(rootDirPath);
        if (!file.exists()) {
            logger.info("could not find rootDirPath = {}", rootDirPath);
            return;
        }

        for (File childFile : file.listFiles()) {
            String fileName = getFileName(childFile);
            logger.info("load config file = {}", fileName);
            String fileString = FileUtil.getConfigFileString(childFile.getName());
            ConstructorAccess<BaseTable> constructorAccess = TableAnnotation.getInstance().getTableMap(fileName);
            BaseTable baseTable = constructorAccess.newInstance();
            baseTable.init(fileString);
        }
    }

    private String getFileName(File file) {
        return file.getName().substring(0, file.getName().lastIndexOf(".")).trim();
    }
}
