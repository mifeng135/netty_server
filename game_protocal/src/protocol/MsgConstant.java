package protocol;

public class MsgConstant {


    /******************************************************************************************/
    public static final int MSG_TCP_REQ = 1;
    public static final int MSG_TCP_RSP = 2;
    public static final int MSG_RECONNECT_REQ = 3;
    public static final int MSG_RECONNECT_RSP = 4;
    public static final int MSG_CENTER_SESSION_REQ = 5;
    public static final int MSG_CENTER_SESSION_RSP = 6;


    /******************************************************************************************/
    public static final int MSG_LOGIN_REQ = 1000;
    public static final int MSG_LOGIN_RSP = 1001;
    public static final int MSG_HEART_BEAT_REQ = 1002;
    public static final int MSG_HEART_BEAT_RSP = 1003;
    public static final int MSG_SOCKET_INDEX_REQ = 1004;
    public static final int MSG_SOCKET_INDEX_RSP = 1005;
    public static final int MSG_REGISTER_REQ = 1006;
    public static final int MSG_REGISTER_RSP = 1007;
    public static final int MSG_SYC_POSITION_REQ = 1008;
    public static final int MSG_SYC_POSITION_RSP = 1009;
    public static final int MSG_ENTER_SCENE_REQ = 1010;
    public static final int MSG_ENTER_SCENE_RSP = 1011;

    /***********************************broadcast**********************************************************/

    public static final int MSG_SYC_POSITION_BC = 10000;
}
