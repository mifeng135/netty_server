package com.game.server.proto;

import java.util.List;

/**
 * Created by Administrator on 2020/7/19.
 */
public class ProtoTilePositionSynR {

    private int mapWidth;
    private int mapHeight;
    private List<TileVec> tileList;

    public List<TileVec> getTileList() {
        return tileList;
    }

    public void setTileList(List<TileVec> tileList) {
        this.tileList = tileList;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public void setMapWidth(int mapWidth) {
        this.mapWidth = mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public void setMapHeight(int mapHeight) {
        this.mapHeight = mapHeight;
    }

    public static class TileVec {
        private int x;
        private int y;

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

    }
}
