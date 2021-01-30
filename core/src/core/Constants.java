package core;

import core.netty.tcp.TcpConnection;
import io.netty.util.AttributeKey;

/**
 * Created by Administrator on 2020/5/28.
 */
public class Constants {

    /**
     * 是否开启 DELAY 算法
     */
    public static final boolean TCP_NO_DELAY_DEFAULT = true;

    /**
     * 是否开启 REUSEADDR
     */
    public static final boolean TCP_SO_REUSEADDR_DEFAULT = true;

    public static final boolean TCP_SO_KEEP_ALIVE_DEFAULT = false;

    /**
     * 是否开启 超时
     */
    public static final boolean NETTY_OPEN_IDLE = true;

    public static final int NETTY_IO_RATIO_DEFAULT = 70;

    public static final int TCP_SO_BACKLOG_DEFAULT = 1024;

    /**
     * 设置超时时间  30秒为 前端为发生请求 则主动关闭连接
     */
    public static final int TCP_SERVER_IDLE_DEFAULT = 30;

    public static final AttributeKey<Integer> PLAYER_INDEX = AttributeKey.valueOf("PLAYER_INDEX");
    public static final AttributeKey<String> REMOTE_ADDRESS = AttributeKey.valueOf("REMOTE_ADDRESS");
    public static final AttributeKey<TcpConnection> TCP = AttributeKey.valueOf("TcpConnection");
    public static final AttributeKey<String> CONNECT_IP = AttributeKey.valueOf("CONNECT_IP");
    public static final AttributeKey<Integer> PORT = AttributeKey.valueOf("PORT");

    public static final String SERVER_PACKAGE_NAME = "com.game.server";

    public static final int TCP_HEADER_LEN = 2; //short

    public static final int MSG_RESULT_SUCCESS = 0;
    public static final int MSG_RESULT_FAIL = 1;

    public static final String IDLE_STATE_HANDLER = "idleStateHandler";

    public static final int LOCAL_SOCKET_RANGE = 1000;

    public static final int LOCAL = 1;
    public static final int REMOTE = 2;

    public static final int REMOTE_MSG_ENCODER_HEADER_LEN = 8; //short(len) int(msgId)  short(result)
    public static final int REMOTE_MSG_DECODER_HEADER_LEN = 10;//short(len) int(socketIndex) int(msgId)

    public static final int LOCAL_MSG_ENCODER_HEADER_LEN = 10; //short(len) int(playerIndex) int(msgId)
    public static final int LOCAL_MSG_DECODER_HEADER_LEN = 10; //short(len) int(playerIndex) int(msgId)
}
