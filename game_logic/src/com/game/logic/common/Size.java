package com.game.logic.common;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Size {
    private int width;
    private int height;

    public Size() {

    }

    public Size(int width, int height) {
        this.width = width;
        this.height = height;
    }
}
