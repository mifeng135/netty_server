package com.game.server.server;

import com.game.server.bean.PlayerBean;
import com.game.server.constant.SqlCmdConstant;
import com.game.server.eventGroup.LoginEventHandler;
import com.game.server.ServerConfig;
import core.annotation.CtrlAnnotation;
import core.annotation.SqlAnnotation;
import core.groupHelper.EventThreadGroup;
import core.netty.HttpServer;
import core.redis.RedisManager;
import core.sql.MysqlBatchHandle;
import core.sql.MysqlBean;


/**
 * Created by Administrator on 2020/6/23.
 */
public class LoginServer {

    public void start() {

        /**开启2条线程去处理登录*/
        new EventThreadGroup(ServerConfig.REGION_LOGIN, LoginEventHandler.class, 2);

        /**启动redis mysql 批处理*/
        RedisManager.getInstance();
        MysqlBatchHandle.getInstance();

        CtrlAnnotation.getInstance();
        SqlAnnotation.getInstance();

        /**开启http登录服务器*/
        new HttpServer(ServerConfig.HTTP_SERVER_IP, ServerConfig.HTTP_PORT, new LoginHttpHandler());


        PlayerBean playerBean = new PlayerBean();
        playerBean.setAccount("111111");
        playerBean.setPassword("111111");
        PlayerBean result = (PlayerBean) SqlAnnotation.getInstance().executeSelectSql(SqlCmdConstant.PLAYER_SELECT_ACCOUNT_PASSWORD, playerBean);
        PlayerBean result1 = (PlayerBean) SqlAnnotation.getInstance().executeSelectSql(SqlCmdConstant.PLAYER_SELECT_ACCOUNT_PASSWORD, playerBean);


        result1.setLastLoginTime(444);
        MysqlBean sqlBean = new MysqlBean();
        sqlBean.setCmd(SqlCmdConstant.PLAYER_UPDATE_LOGIN_INFO);
        sqlBean.setData(result1);
        MysqlBatchHandle.getInstance().pushMsg(sqlBean);
    }
}
