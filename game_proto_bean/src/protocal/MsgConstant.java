package protocal;


public class MsgConstant {
    /***************************************************************************************/

    public static final int MSG_SYSTEM_EXCEPTION_PUSH = 1;

    /***********************************db center**************************************************/

    public static final int DB_CMD_QUERY_ALL_PLAYER_INFO_REQ = 1001;                //查询玩家信息
    public static final int DB_CMD_CREATE_PLAYER_REQ = 1002;                        //创建角色

    /************************************client center******************************************************/

    public static final int MSG_SERVER_LIST_REQ = 10000;                        //获取服务器列表
    public static final int MSG_SERVER_LIST_RSP = 10001;

    public static final int MSG_NOTICE_LIST_REQ = 10002;                        //获取公告
    public static final int MSG_NOTICE_LIST_RSP = 10003;

    public static final int MSG_REMOTE_OPEN_SOCKET_REQ = 10004;                 //客户端连接socket
    public static final int MSG_REMOTE_OPEN_SOCKET_RSP = 10005;

    public static final int MSG_ENTER_GAME_REQ = 10006;                         //进入游戏
    public static final int MSG_ENTER_GAME_RSP = 10007;

    public static final int MSG_CREATE_PLAYER_REQ = 10008;                      //创建角色
    public static final int MSG_CREATE_PLAYER_RSP = 10009;

    public static final int MSG_HEART_BEAT_REQ = 10010;                         //心跳
    public static final int MSG_HEART_BEAT_RSP = 10011;

    public static final int MSG_RECONNECT_REQ = 10012;                          //客户端重连
    public static final int MSG_RECONNECT_RSP = 10013;

    public static final int MSG_GET_PLAYER_INDEX_REQ = 10014;                   //这个暂时不用了
    public static final int MSG_GET_PLAYER_INDEX_RSP = 10015;

    public static final int MSG_SYNC_POSITION_REQ = 10016;                       //位置同步
    public static final int MSG_SYNC_POSITION_RSP = 10017;


    /*********************************PUSH**********************************************************/
    /**
     * 主动推送
     */
    public static final int MSG_REPLACE_ACCOUNT_PUSH = 200000;                   //顶号
    public static final int MSG_REMOTE_SOCKET_CLOSE_PUSH = 200001;               //客户端断开连接
    public static final int MSG_LOCAL_SOCKET_CLOSE_PUSH = 200002;                //本地服务器断开连接
    public static final int MSG_LOCAL_OPEN_SOCKET_PUSH = 200003;                 //本地socket发送连接请求
    public static final int MSG_REGISTER_MSG_CMD_PUSH = 200004;                  //其他服务器向gate注册消息 注册以后的消息 gate才可以向其转发
}
