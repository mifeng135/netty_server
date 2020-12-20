package core.util;

import core.Configs;
import core.SocketInfo;
import core.manager.SocketManager;
import core.zero.MPairClientSocket;
import core.zero.MPairServerSocket;
import core.zero.MPairSocket;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2020/12/5.
 */
public class HelperUtil {

    private static AtomicInteger serverInteger = new AtomicInteger(0);

    /**
     * 生成server 唯一 key
     *
     * @return int serverkey
     */
    public static int generateServerKey() {
        return serverInteger.incrementAndGet();
    }


    public static void startSocket(List<SocketInfo> list) {
        for (int i = 0; i < list.size(); i++) {
            SocketInfo socketInfo = list.get(i);
            int socketType = socketInfo.getSocketType();
            MPairSocket pairSocket;
            if (socketType == Configs.SOCKET_TYPE_CLIENT) {
                pairSocket = new MPairClientSocket(socketInfo.getIp(), socketInfo.getPort(), socketInfo.getSocketKey(), socketInfo.getAdapter());
            } else {
                pairSocket = new MPairServerSocket(socketInfo.getIp(), socketInfo.getPort(), socketInfo.getSocketKey(), socketInfo.getAdapter());
            }
            ((Thread) pairSocket).start();
            SocketManager.getInstance().putSocket(socketInfo.getSocketKey(), pairSocket);
        }
    }
}
