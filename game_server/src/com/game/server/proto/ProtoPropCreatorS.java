package com.game.server.proto;

import java.util.List;

/**
 * Created by Administrator on 2020/7/18.
 */
public class ProtoPropCreatorS {
    private List<PropVec> propList;
    public List<PropVec> getPropList() {
        return propList;
    }
    public void setPropList(List<PropVec> propList) {
        this.propList = propList;
    }
    public static class PropVec{
        private int x;
        private int y;
        private int type;

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

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
