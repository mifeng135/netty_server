package core.manager;

import core.msg.TransferMsg;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/*****
 * message route map
 */
public class LocalRouterSocketManager {

    private static Logger logger = LoggerFactory.getLogger(LocalRouterSocketManager.class);

    private Map<Integer, Set<Integer>> routerMap = new HashMap<>();

    private static class DefaultInstance {
        static final LocalRouterSocketManager INSTANCE = new LocalRouterSocketManager();
    }

    public static LocalRouterSocketManager getInstance() {
        return DefaultInstance.INSTANCE;
    }

    private LocalRouterSocketManager() {

    }

    public void addRouter(int socketIndex, Set<Integer> set) {
        routerMap.put(socketIndex, set);
        logger.info("addRouter success socketIndex = {}", socketIndex);
    }

    public Map<Integer, Set<Integer>> getRouterMap() {
        return routerMap;
    }

    public void sendRouterMsg(TransferMsg msg) {
        int msgId = msg.getHeaderProto().getMsgId();
        routerMap.forEach((socketIndex, value) -> {
            if (value.contains(msgId)) {
                Channel channel = LocalSocketManager.getInstance().getChanel(socketIndex);
                if (channel != null && channel.isActive()) {
                    channel.writeAndFlush(msg);
                }
            }
        });
    }
}
