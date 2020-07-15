package com.game.server.proto;

import java.util.List;

/**
 * Created by Administrator on 2020/7/15.
 */
public class ProtoBombExplodeR {

    private List<Vec> explodePath;

    public List<Vec> getExplodePath() {
        return explodePath;
    }

    public void setExplodePath(List<Vec> explodePath) {
        this.explodePath = explodePath;
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



