package com.game.db;

import com.game.db.server.DBServer;

import java.io.IOException;

/**
 * Created by Administrator on 2020/6/1.
 */
public class DB {

    public static void main(String[] args) throws IOException {
        new DBServer().start();
    }
}
