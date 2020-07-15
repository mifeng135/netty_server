package com.game.server.map;

/**
 * Created by Administrator on 2020/7/15.
 */
public class MapUtil {


    public static final int MAP_TILE_WIDTH_COUNT = 15; //地图tile 横向数量
    public static final int MAP_TILE_HEIGHT_COUNT = 10; // 地图tile 纵向数量

    public static final int MAP_TILE_WIDTH = 68; //tile 宽
    public static final int MAP_TILE_HEIGHT = 64;//tile 高

    public static MapVec getTilePosition(float playerX, float playerY) {
        int mapHeight = MAP_TILE_HEIGHT_COUNT * MAP_TILE_HEIGHT;
        int x = (int) Math.floor(playerX / MAP_TILE_WIDTH);
        int y = (int) Math.floor((mapHeight - playerY) / MAP_TILE_HEIGHT);
        return new MapVec(x, y);
    }
}
