package com.game.server.constant;


/**
 * Created by Administrator on 2020/6/15.
 */
public class SqlCmdConstant {


    /********************************PLAYER INFO***********************************/
    public static final int PLAYER_INFO_SELECT_ONE                                      = 110;
    public static final int PLAYER_INFO_INSERT                                          = 111;
    public static final int PLAYER_INFO_MAX_PLAYER_ID                                   = 112;

    /*********************************SERVER LIST********************************************/
    public static final int SERVER_LIST_SELECT_ALL                                      = 130; //获取所有的服务器列表
    public static final int SERVER_LIST_SELECT_ONE                                      = 131; //通过id获取服务器某个列表
    public static final int SERVER_LIST_UPDATE_SERVER_NAME                              = 132; //更新服务器名称
    public static final int SERVER_LIST_UPDATE_SERVER_STATE                             = 133; //更新服务器状态
    public static final int SERVER_LIST_DELETE_SERVER                                   = 134; //删除某个服务器
    public static final int SERVER_LIST_INSERT_SERVER                                   = 135; //插入服务器
    public static final int SERVER_LIST_UPDATE_SERVER_IP                                = 136; //更新服务器ip

    /********************************* NOTICE ********************************************/
    public static final int NOTICE_LIST_SELECT_ALL                                      = 140;
    public static final int NOTICE_UPDATE_CONTENT                                       = 141;
    public static final int NOTICE_DELETE                                               = 142;
}
