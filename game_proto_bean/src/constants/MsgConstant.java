package constants;

public class MsgConstant {
    /***************************************************************************************/


    /***********************************db 和db相关都操作都定义到这个地方************************************/
    public static final int DB_CMD_BEGIN = 1000;
    public static final int DB_CMD_QUERY_PLAYER_ALL_INFO_REQ = 1001;                //查询玩家信息
    public static final int DB_CMD_CREATE_PLAYER_REQ = 1003;                        //创建角色
    public static final int DB_CMD_END = 9999;

    /************************************login******************************************************/

    public static final int MSG_SERVER_LIST_REQ = 10000;                        //获取服务器列表
    public static final int MSG_SERVER_LIST_RSP = 10001;

    public static final int MSG_NOTICE_LIST_REQ = 10002;                        //获取公告
    public static final int MSG_NOTICE_LIST_RSP = 10003;

    /************************************client center******************************************************/

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

    public static final int MSG_SYNC_POSITION_REQ = 10016;                       //位置同步
    public static final int MSG_SYNC_POSITION_RSP = 10017;


    /*********************************PUSH**********************************************************/
    /**
     * 主动推送
     */
    public static final int MSG_REPLACE_ACCOUNT_PUSH = 200000;                   //顶号


    /*********************************HTTP URL**********************************************************/

    public static final int MSG_CLIENT_ERROR_REPORT = 10000000;                 //客户端异常上报

    public static final String HTTP_URL_GET_SERVER_LIST = "/getServerList";
    public static final String HTTP_URL_GET_NOTICE = "/getNotice";
}
