package com.game.server.constant;


/**
 * Created by Administrator on 2020/6/15.
 */
public class SqlCmdConstant {
    public static final int PLAYER_SELECT_ACCOUNT_PASSWORD                              = 1;
    public static final int PLAYER_UPDATE_LOGIN_INFO                                    = 2;
    public static final int PLAYER_INSERT_REGISTER                                      = 3;
    public static final int PLAYER_SELECT_ACCOUNT                                       = 4;

    /********************************PLAYER SCENE***********************************/

    public static final int PLAYER_SCENE_SELECT_SCENE_INFO                              = 100; //获取人物地图数据
    public static final int PLAYER_SCENE_UPDATE_SCENE_INFO                              = 101; //更新人物地图信息
    public static final int PLAYER_SCENE_DELETE_SCENE_INFO                              = 102; //删除人物地图信息
    public static final int PLAYER_SCENE_INSERT_SCENE_INFO                              = 103; //生成人物地图信息(生在新手村）

    /********************************PLAYER ITEM***********************************/
    public static final int PLAYER_ITEM_SELECT_ALL_ITEM                                 = 200; //获取某个玩家的所有物品
    public static final int PLAYER_ITEM_SELECT_ONE_ITEM                                 = 201; //获取某个玩家的某个物品
    public static final int PLAYER_ITEM_UPDATE_ITEM                                     = 202; //更新某个玩家的物品
    public static final int PLAYER_ITEM_DELETE_ITEM                                     = 203; //删除某个玩家的某个物品
    public static final int PLAYER_ITEM_INSERT_ITEM                                     = 204; //给某个玩家插入某个物品


    /*********************************SERVER LIST********************************************/
    public static final int SERVER_LIST_SELECT_ALL                                      = 300; //获取所有的服务器列表
    public static final int SERVER_LIST_SELECT_ONE                                      = 301; //通过id获取服务器列表
    public static final int SERVER_LIST_UPDATE_SERVER_NAME                              = 302; //更新服务器名称
    public static final int SERVER_LIST_UPDATE_SERVER_STATE                             = 303; //更新服务器状态
    public static final int SERVER_LIST_DELETE_SERVER                                   = 304; //删除某个服务器
    public static final int SERVER_LIST_INSERT_SERVER                                   = 305; //插入服务器
    public static final int SERVER_LIST_UPDATE_SERVER_IP                                = 306; //更新服务器ip
}
