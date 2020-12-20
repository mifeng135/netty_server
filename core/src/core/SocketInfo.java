package core;


import core.zero.MSocketAdapter;
import lombok.Getter;

/**
 * Created by Administrator on 2020/7/28.
 */

@Getter
public class SocketInfo {
    private String ip;
    private int port;
    private String socketKey;
    private MSocketAdapter adapter;
    private int socketType;

    /**
     * init receive socket info
     *
     * @param ip
     * @param port
     * @param socketKey
     * @param adapter
     * @param socketType
     */
    public SocketInfo(String ip, int port, String socketKey, int socketType, MSocketAdapter adapter) {
        this.ip = ip;
        this.port = port;
        this.adapter = adapter;
        this.socketKey = socketKey;
        this.socketType = socketType;
    }
}
