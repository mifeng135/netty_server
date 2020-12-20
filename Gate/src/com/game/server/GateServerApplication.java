package com.game.server;

import com.game.server.server.GateServer;


/**
 * Created by Administrator on 2020/6/1.
 */
public class GateServerApplication {

    public static void main(String[] arg) throws Exception {
        new GateServer().start();
    }
}
