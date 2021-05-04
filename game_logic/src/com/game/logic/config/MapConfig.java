package com.game.logic.config;


import com.alibaba.fastjson.JSON;
import core.BaseTable;
import core.annotation.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Setter
@Getter
@Table(name = "MapConfig")
public class MapConfig implements BaseTable {
    private int id;
    private String mapName;
    private int width;
    private int height;
    private int cellWidth;
    private int cellHeight;

    //private static Map<Integer, MapConfig> data;

    public static List<MapConfig> mapConfigList;

    @Override
    public void init(String jsonStr) {
        mapConfigList = JSON.parseArray(jsonStr, MapConfig.class);
        //data = mapConfigList.stream().collect(Collectors.toMap(MapConfig::getId, (p) -> p));
    }
}
