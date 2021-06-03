package core.redis;

import com.alibaba.fastjson.JSON;
import com.moilioncircle.redis.replicator.Configuration;
import com.moilioncircle.redis.replicator.RedisReplicator;
import com.moilioncircle.redis.replicator.Replicator;
import com.moilioncircle.redis.replicator.cmd.Command;
import com.moilioncircle.redis.replicator.cmd.CommandName;
import com.moilioncircle.redis.replicator.cmd.CommandParser;
import com.moilioncircle.redis.replicator.cmd.impl.HSetCommand;
import com.moilioncircle.redis.replicator.event.Event;
import com.moilioncircle.redis.replicator.event.EventListener;
import com.moilioncircle.redis.replicator.io.RawByteListener;
import core.util.ProtoUtil;
import io.netty.buffer.ByteBufInputStream;
import org.nutz.json.Json;

import java.io.IOException;
import java.util.Map;

public class MFReplicator extends Thread {
    private final Replicator replicator;

    public MFReplicator(String host, int port, String password) {
        Configuration configuration = Configuration.defaultSetting();
        configuration.setAuthPassword("nqwl0520");
        replicator = new RedisReplicator("127.0.0.1", 6379, configuration);

        replicator.addEventListener(new EventListener() {
            @Override
            public void onEvent(Replicator replicator, Event event) {
                if (event instanceof HSetCommand) {
                    Map<byte[], byte[]> data = ((HSetCommand) event).getFields();

                    for (Map.Entry<byte[], byte[]> entry : data.entrySet()) {
                        byte[] mapKey = entry.getKey();
                        byte[] mapValue = entry.getValue();
                        String key = new String(mapKey);
                        String value = new String(mapValue);


                        long time = System.currentTimeMillis();
                        Object oc = JSON.parseObject(value, Object.class);

                        long last = System.currentTimeMillis();

                        System.out.println(last - time);


                        System.out.println(Thread.currentThread().getName());
                        int mm1 = 0;
                    }
                }
            }
        });
        replicator.addRawByteListener(new RawByteListener() {
            @Override
            public void handle(byte... bytes) {
                int mm = 0;
            }
        });
        replicator.addCommandParser(CommandName.name("SET"), new YourAppendParser());
    }

    public static class YourAppendParser implements CommandParser<YourAppendParser.YourAppendCommand> {

        @Override
        public YourAppendCommand parse(Object[] command) {
            return new YourAppendCommand((String) command[1], (String) command[2]);
        }

        public static class YourAppendCommand implements Command {
            public final String key;
            public final String value;

            public YourAppendCommand(String key, String value) {
                this.key = key;
                this.value = value;
            }

            @Override
            public String toString() {
                return "YourAppendCommand{" +
                        "key='" + key + '\'' +
                        ", value='" + value + '\'' +
                        '}';
            }
        }
    }

    @Override
    public void run() {
        try {
            replicator.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
