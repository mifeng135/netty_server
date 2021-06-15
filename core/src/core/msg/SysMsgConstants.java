package core.msg;

public class SysMsgConstants {
    public static final int MSG_LOCAL_OPEN_SOCKET_PUSH = 1;                 //本地服务器之间socket打开链接 //
    public static final int MSG_LOCAL_SOCKET_CLOSE_PUSH = 2;                //本地服务器之间socket断开链接
    public static final int MSG_REMOTE_SOCKET_CLOSE_PUSH = 3;               //远程client断开链接
    public static final int MSG_REGISTER_MSG_CMD_PUSH = 4;                  //其他服务器向gate注册消息 注册以后的消息 gate才可以向其转发
    public static final int MSG_SYSTEM_EXCEPTION_PUSH = 100;

}
