package protocal;


public class MsgConstant {


    /**********************************system********************************************************/
    public static final int MSG_LOCAL_SOCKET_REQ = 1;                           //本地socket发送连接请求
    public static final int MSG_LOCAL_SOCKET_RSP = 2;                           //注册转发消息

    public static final int MSG_CENTER_SESSION_REQ = 5;                         //gate向中心副注册客户端新的链接
    public static final int MSG_CENTER_SESSION_RSP = 6;

    public static final int MSG_CLOSE_SOCKET_REQ = 7;                           //客户端断开链接
    public static final int MSG_CLOSE_SOCKET_RSP = 8;


    /***********************************db server**************************************************/

    public static final int MSG_DB_QUERY_LOGIN = 100;                            //通过帐号密码得到人物基本信息
    public static final int MSG_DB_QUERY_SCENE = 101;
    public static final int MSG_DB_QUERY_ENTER_SCENE = 102;


    /************************************client server******************************************************/

    public static final int MSG_GET_SERVER_LIST_REQ = 10000;                    //获取服务器列表
    public static final int MSG_GET_SERVER_LIST_RSP = 10001;

    public static final int MSG_LOGIN_REQ = 10001;                               //login
    public static final int MSG_LOGIN_RSP = 10002;

    public static final int MSG_HEART_BEAT_REQ = 10002;                          //心跳
    public static final int MSG_HEART_BEAT_RSP = 10003;

    public static final int MSG_SOCKET_LOGIN_REQ = 10004;                        //socket 设置 playerindex
    public static final int MSG_SOCKET_LOGIN_RSP = 10050;

    public static final int MSG_RECONNECT_REQ = 10006;                           //客户端重连
    public static final int MSG_RECONNECT_RSP = 10007;

    public static final int MSG_SYNC_POSITION_REQ = 10008;                       //位置同步
    public static final int MSG_SYNC_POSITION_RSP = 10009;


    /*********************************PUSH**********************************************************/
    /**
     * 主动推送
     */
    public static final int MSG_REPLACE_ACCOUNT_PUSH = 200000;                  //客户端断线
}
