package core.manager;

import io.netty.channel.Channel;
import io.netty.util.internal.PlatformDependent;

import java.util.concurrent.ConcurrentMap;

/**
 * Created by Administrator on 2020/12/6.
 */
public class HttpConnectManager {

    public static final ConcurrentMap<Integer, Channel> httpChannelMap = PlatformDependent.newConcurrentHashMap();

    public static void putConnect(int playerIndex, Channel channel) {
        httpChannelMap.put(playerIndex, channel);
    }

    public static Channel removeConnect(int playerIndex) {
        return httpChannelMap.remove(playerIndex);
    }

    public static Channel getConnect(int playerIndex) {
        return httpChannelMap.get(playerIndex);
    }
}
