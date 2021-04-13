package com.game.server.constant;


import org.omg.CORBA.PUBLIC_MEMBER;

/**
 * Created by Administrator on 2020/6/15.
 */
public class SqlCmdConstant {



    /********************************PLAYER SCENE***********************************/

    public static final int PLAYER_SCENE_SELECT_SCENE_INFO                              = 100; //获取人物地图数据
    public static final int PLAYER_SCENE_UPDATE_SCENE_INFO                              = 101; //更新人物地图信息
    public static final int PLAYER_SCENE_DELETE_SCENE_INFO                              = 102; //删除人物地图信息
    public static final int PLAYER_SCENE_INSERT_SCENE_INFO                              = 103; //生成人物地图信息(生在新手村）


    /********************************PLAYER INFO***********************************/
    public static final int PLAYER_INFO_SELECT_ONE                                      = 110;
    public static final int PLAYER_INFO_UPDATE_LOGIN                                    = 111;
    public static final int PLAYER_INFO_UPDATE_HEADER                                   = 112;
    public static final int PLAYER_INFO_DELETE                                          = 113;
    public static final int PLAYER_INFO_INSERT                                          = 114;

    /********************************PLAYER ITEM***********************************/
    public static final int PLAYER_ITEM_SELECT_ALL_ITEM                                 = 120; //获取某个玩家的所有物品
    public static final int PLAYER_ITEM_SELECT_ONE_ITEM                                 = 121; //获取某个玩家的某个物品
    public static final int PLAYER_ITEM_UPDATE_ITEM                                     = 122; //更新某个玩家的物品
    public static final int PLAYER_ITEM_DELETE_ITEM                                     = 123; //删除某个玩家的某个物品
    public static final int PLAYER_ITEM_INSERT_ITEM                                     = 124; //给某个玩家插入某个物品


    /*********************************SERVER LIST********************************************/
    public static final int SERVER_LIST_SELECT_ALL                                      = 130; //获取所有的服务器列表
    public static final int SERVER_LIST_SELECT_ONE                                      = 131; //通过id获取服务器某个列表
    public static final int SERVER_LIST_UPDATE_SERVER_NAME                              = 132; //更新服务器名称
    public static final int SERVER_LIST_UPDATE_SERVER_STATE                             = 133; //更新服务器状态
    public static final int SERVER_LIST_DELETE_SERVER                                   = 134; //删除某个服务器
    public static final int SERVER_LIST_INSERT_SERVER                                   = 135; //插入服务器
    public static final int SERVER_LIST_UPDATE_SERVER_IP                                = 136; //更新服务器ip
}
