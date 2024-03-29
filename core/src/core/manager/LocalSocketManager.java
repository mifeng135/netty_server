package core.manager;

import core.Constants;
import core.msg.TransferMsg;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LocalSocketManager {

    private static Logger logger = LoggerFactory.getLogger(LocalSocketManager.class);

    private ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private Map<Integer, Channel> channelMap = new ConcurrentHashMap<>();

    private static class DefaultInstance {
        static final LocalSocketManager INSTANCE = new LocalSocketManager();
    }

    public static LocalSocketManager getInstance() {
        return DefaultInstance.INSTANCE;
    }

    public Channel getChanel(int socketIndex) {
        return channelMap.get(socketIndex);
    }

    public void putChannel(int playerIndex, Channel channel) {
        channelMap.put(playerIndex, channel);
        channelGroup.add(channel);
    }

    public void removeChannel(int playerIndex) {
        channelMap.remove(playerIndex);
    }

    public void removeChannel(Channel channel) {
        int playerIndex = channel.attr(Constants.PLAYER_INDEX).get();
        removeChannel(playerIndex);
        logger.info("disconnect playerIndex = {}", playerIndex);
    }

    public void broadcast(TransferMsg msg) {
        channelGroup.writeAndFlush(msg);
    }
}
