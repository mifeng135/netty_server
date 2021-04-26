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
}
