package core.redis;

import com.alibaba.fastjson.JSON;
import com.moilioncircle.redis.replicator.Configuration;
import com.moilioncircle.redis.replicator.RedisReplicator;
import com.moilioncircle.redis.replicator.Replicator;
import com.moilioncircle.redis.replicator.cmd.Command;
import com.moilioncircle.redis.replicator.cmd.CommandParser;
import com.moilioncircle.redis.replicator.cmd.impl.EvalCommand;
import com.moilioncircle.redis.replicator.cmd.impl.HDelCommand;
import com.moilioncircle.redis.replicator.cmd.impl.HSetCommand;
import com.moilioncircle.redis.replicator.event.Event;
import com.moilioncircle.redis.replicator.event.EventListener;


import java.io.IOException;
import java.util.Map;

public class MFReplicator extends Thread {
    private final Replicator replicator;
    private ScheduledToMysql scheduledToMysql = new ScheduledToMysql();


    public MFReplicator(String host, int port, String password) {
        Configuration configuration = Configuration.defaultSetting();
        configuration.setAuthPassword("nqwl0520");
        replicator = new RedisReplicator("127.0.0.1", 6379, configuration);

        replicator.addEventListener(new EventListener() {
            @Override
            public void onEvent(Replicator replicator, Event event) {

                System.out.println(event.toString());
                if (event instanceof HSetCommand) {
                    Map<byte[], byte[]> data = ((HSetCommand) event).getFields();
                    for (Map.Entry<byte[], byte[]> entry : data.entrySet()) {
                        byte[] mapValue = entry.getValue();
                        Object oc = JSON.parseObject(mapValue, Object.class);
                    }
                } else if (event instanceof EvalCommand) {
                    EvalCommand evalCommand = (EvalCommand) event;
                    int keyNumber = evalCommand.getNumkeys();
                    String key = new String(evalCommand.getKeys()[0]);
                    String arg = new String(evalCommand.getArgs()[0]);
                    String mm = new String(evalCommand.getScript());
                    System.out.println(mm);
                    int mm1 = 0;
                } else if (event instanceof HDelCommand) {
                    byte[][] data = ((HDelCommand) event).getFields();
                }
            }
        });
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
