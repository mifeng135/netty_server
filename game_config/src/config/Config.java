package config;

public class Config {

    public static final String HTTP_IP = "127.0.0.1";
    public static final int HTTP_PORT = 8000;

    public static final String DB_HTTP_SERVER_IP = "127.0.0.1";
    public static final int DB_HTTP_SERVER_PORT = 8001;


    public static final String ADMIN_HTTP_SERVER_IP = "127.0.0.1";
    public static final int ADMIN_HTTP_PORT = 8002;


    public static final String DB_HTTP_URL = "http://" + DB_HTTP_SERVER_IP + ":" + DB_HTTP_SERVER_PORT;


    public static final String REDIS_IP = "redis://127.0.0.1:6379";
    public static final String REDIS_PWD = "nqwl0520";

    public static final int REDIS_THREAD_COUNT = 2;
    public static final int REDIS_NETTY_THREAD_COUNT = 2;

    /****************************server ip************************************/
    public static final String GATE_SERVER_IP = "127.0.0.1";
    public static final int GATE_SERVER_PORT = 7000;

    public static final String CENTER_SERVER_IP = "127.0.0.1";
    public static final int CENTER_SERVER_PORT = 7001;

    public static final String SCENE_SERVER_IP = "127.0.0.1";
    public static final int SCENE_SERVER_PORT = 7002;


    /****************************connect socket index**************************/
    public static final int GATE_CENTER_SOCKET_INDEX = 1; //gate 到 center 连接
    public static final int GATE_SCENE_SOCKET_INDEX = 2; //gate 到 scene 连接
    public static final int SCENE_CENTER_SOCKET_INDEX = 3; //scene 到 center 连接
}
