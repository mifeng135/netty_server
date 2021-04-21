package com.game.login;


import bean.login.NoticeBean;
import bean.login.PlayerLoginBean;
import bean.player.PlayerServerInfoBean;
import com.game.login.query.NoticeListQuery;
import com.game.login.query.PlayerInfoQuery;
import com.game.login.query.ServerListQuery;
import com.game.login.redis.RedisCache;
import core.annotation.CtrlAnnotation;
import core.annotation.SqlAnnotation;
import core.group.EventThreadGroup;
import core.netty.http.HttpServer;
import core.redis.RedisManager;
import core.sql.SqlDao;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.redisson.api.RMap;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static core.Constants.SQL_MASTER;

/**
 * Created by Administrator on 2020/6/1.
 */
public class LoginApplication {

    public static void main(String[] args) {
        SqlDao.getInstance().init(SQL_MASTER, "dao.properties","pre-sql.sqls");
        new ProperticeConfig();
        CtrlAnnotation.getInstance().init(LoginApplication.class.getPackage().getName());
        RedisManager.getInstance().init(ProperticeConfig.redisIp, ProperticeConfig.redisPassword,
                ProperticeConfig.redisThreadCount, ProperticeConfig.redisNettyThreadCount);
        RedisCache.getInstance();
        new HttpServer(ProperticeConfig.httpIp, ProperticeConfig.httpPort);
        new EventThreadGroup(Runtime.getRuntime().availableProcessors(), LoginApplication.class.getName());


        long t1 = System.currentTimeMillis();
         Sql sql = SqlDao.getInstance().getDao(SQL_MASTER).sqls().create("select_player_server.data");
        long t2 = System.currentTimeMillis();
        System.out.println(t2 - t1);

        t1 = System.currentTimeMillis();
        Sql sql12 = SqlDao.getInstance().getDao(SQL_MASTER).sqls().create("test.data");
        t2 = System.currentTimeMillis();
        System.out.println(t2 - t1);
        sql.setCallback((conn, rs, sql1) -> {
            List<PlayerServerInfoBean> list = new LinkedList<>();
            while (rs.next()) {
                PlayerServerInfoBean loginBean = SqlDao.getInstance().getDao(SQL_MASTER).
                        getEntity(PlayerServerInfoBean.class).
                        getObject(rs,null,"");
                list.add(loginBean);
            }
            return list;
        });
        SqlDao.getInstance().getDao(SQL_MASTER).execute(sql);
        List<PlayerServerInfoBean> list = sql.getList(PlayerServerInfoBean.class);
        Map<Integer, List<PlayerServerInfoBean>> groupBy = list.stream().collect(Collectors.groupingBy(PlayerServerInfoBean::getPlayerIndex));


        ServerListQuery.deleteServer(1);
    }
}
