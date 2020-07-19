package com.game.server.proto;

import java.util.List;

/**
 * Created by Administrator on 2020/7/18.
 */
public class ProtoPropCreatorR {

    private List<Vec> removePath;

    public List<Vec> getRemovePath() {
        return removePath;
    }

    public void setRemovePath(List<Vec> removePath) {
        this.removePath = removePath;
    }

    public static class Vec{
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
