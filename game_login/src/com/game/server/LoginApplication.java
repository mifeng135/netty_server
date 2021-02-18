package com.game.server;


import core.group.EventThreadGroup;
import core.netty.asyncHttp.AsyncHttp;
import core.netty.http.HttpServer;
import core.redis.RedisManager;
import core.util.ProtoUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Response;
import protocol.MsgConstant;
import protocol.local.db.login.DBLoginReq;

import java.util.concurrent.ExecutionException;

import static config.Config.*;
import static org.asynchttpclient.Dsl.asyncHttpClient;

/**
 * Created by Administrator on 2020/6/1.
 */
public class LoginApplication {

    public static void main(String[] args) {
        AsyncHttp.getInstance();
        RedisManager.getInstance().init(REDIS_IP, REDIS_PWD, REDIS_THREAD_COUNT, REDIS_NETTY_THREAD_COUNT);
        new EventThreadGroup(Runtime.getRuntime().availableProcessors(), LoginApplication.class.getName());
        new HttpServer(HTTP_IP, HTTP_PORT);
    }
}
