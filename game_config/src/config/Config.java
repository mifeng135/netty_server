package config;

public class Config {

    /****************************login************************************/
    public static final String HTTP_IP = "127.0.0.1";
    public static final int HTTP_PORT = 8000;


    public static final int LOGIN_LOGIC_THREAD_COUNT = 4;
    public static final String LOGIN_LOGIC_THREAD_NAME = "login";

    public static final String REDIS_IP = "redis://127.0.0.1:6379";
    public static final String REDIS_PWD = "nqwl0520";

    public static final int REDIS_THREAD_COUNT = 2;
    public static final int REDIS_NETTY_THREAD_COUNT = 2;

    /****************************gate************************************/

    public static final String GATE_SERVER_IP = "127.0.0.1";
    public static final int GATE_SERVER_PORT = 7000;

    public static final int GATE_CENTER_SOCKET_INDEX = 1;

    public static final int GATE_LOGIC_THREAD_COUNT = 4;
    public static final String GATE_LOGIC_THREAD_NAME = "gate";

    /****************************scene************************************/
    public static final int SCENE_LOGIC_THREAD_COUNT = 4;
    public static final String SCENE_LOGIN_LOGIC_THREAD_NAME = "scene";
    public static final int SCENE_CENTER_SOCKET_INDEX = 2;

    /****************************center************************************/

    public static final String CENTER_SERVER_IP = "127.0.0.1";
    public static final int CENTER_SERVER_PORT = 7001;

    public static final int CENTER_LOGIC_THREAD_COUNT = 4;
    public static final String CENTER__LOGIC_THREAD_NAME = "center";


}
