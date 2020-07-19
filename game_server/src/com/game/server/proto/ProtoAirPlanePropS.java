package com.game.server.proto;

import java.util.List;

/**
 * Created by Administrator on 2020/7/19.
 */
public class ProtoAirPlanePropS {

    private List<Vec> propList;
    public List<Vec> getPropList() {
        return propList;
    }

    public void setPropList(List<Vec> propList) {
        this.propList = propList;
    }
    public static class Vec{
        private int x;
        private int y;
        private int type;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }
    }
}
