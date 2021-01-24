package com.game.server;


import core.group.EventThreadGroup;

/**
 * Created by Administrator on 2020/6/1.
 */
public class DBServerApplication {

    public static void main(String[] args) {
        new EventThreadGroup(2);
    }
}
