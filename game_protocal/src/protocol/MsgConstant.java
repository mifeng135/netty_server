package protocol;

import org.springframework.scheduling.config.ScheduledTask;

public class MsgConstant {


    /**********************************system********************************************************/
    public static final int MSG_TCP_REQ = 1;                                    //本地socket发送连接请求
    public static final int MSG_TCP_RSP = 2;
    public static final int MSG_RECONNECT_REQ = 3;                              //客户端重连
    public static final int MSG_RECONNECT_RSP = 4;
    public static final int MSG_CENTER_SESSION_REQ = 5;                         //gate向中心副注册客户端新的链接
    public static final int MSG_CENTER_SESSION_RSP = 6;
    public static final int MSG_CLOSE_SOCKET_REQ = 7;                           //客户端断开链接 向中心服发送
    public static final int MSG_CLOSE_SOCKET_RSP = 8;


    /***********************************db server**************************************************/

    public static final int MSG_DB_PLAYER_INFO_LOGIN_REQ = 100;                 //通过帐号密码得到人物基本信息
    public static final int MSG_DB_PLAYER_INFO_LOGIN_RSP = 101;


    /************************************client server******************************************************/
    public static final int MSG_LOGIN_REQ = 1000;                               //login
    public static final int MSG_LOGIN_RSP = 1001;
    public static final int MSG_HEART_BEAT_REQ = 1002;                          //心跳
    public static final int MSG_HEART_BEAT_RSP = 1003;
    public static final int MSG_SOCKET_INDEX_REQ = 1004;                        //socket 设置 playerindex
    public static final int MSG_SOCKET_INDEX_RSP = 1005;
    public static final int MSG_REGISTER_REQ = 1006;                            //注册
    public static final int MSG_REGISTER_RSP = 1007;
    public static final int MSG_SYC_POSITION_REQ = 1008;                        //位置同步
    public static final int MSG_SYC_POSITION_RSP = 1009;
    public static final int MSG_ENTER_SCENE_REQ = 1010;                         //玩家进入某个地图场景
    public static final int MSG_ENTER_SCENE_RSP = 1011;
    public static final int MSG_REPLACE_ACCOUNT_REQ = 1012;                     //顶号
    public static final int MSG_REPLACE_ACCOUNT_RSP = 1013;

    /***********************************broadcast**********************************************************/

    public static final int MSG_SYC_POSITION_BC = 10000;                        //位置广播
}
