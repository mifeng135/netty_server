package com.game.logic.common;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Rect {
    private Position position;
    private Size size;

    public Rect(int x, int y, int width, int height) {
        position = new Position(x, y);
        size = new Size(width, height);
    }

    public Rect(Position position, Size size) {
        this.position = position;
        this.size = size;
    }


    public int getMaxX() {
        return position.getX() + size.getWidth();
    }

    public int getMaxY() {
        return position.getY() + size.getHeight();
    }


    public int getMinX() {
        return position.getX();
    }

    public int getMinY() {
        return position.getY();
    }

    public boolean containsPoint(Position position) {
        boolean bRet = false;
        if (position.getX() >= getMinX() && position.getX() <= getMaxX() && position.getY() >= getMinY() && position.getY() <= getMaxY()) {
            bRet = true;
        }
        return bRet;
    }
}
