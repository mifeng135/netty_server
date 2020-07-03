package com.game.server;


import com.game.server.server.LoginServer;
import java.io.IOException;

/**
 * Created by Administrator on 2020/6/1.
 */
public class Main {

    public static void main(String[] args) throws IOException {
        new LoginServer().start();
    }
}
