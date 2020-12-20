package com.game.server;


import com.game.server.server.LoginServer;
import core.zero.MPairServerSocket;

import java.io.IOException;

/**
 * Created by Administrator on 2020/6/1.
 */
public class LoginServerApplication {

    public static void main(String[] args) throws IOException {
        new LoginServer().start();
    }
}
