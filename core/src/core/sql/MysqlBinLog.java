package core.sql;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.deserialization.EventDeserializer;
import core.util.Instance;

import java.io.IOException;

public class MysqlBinLog extends Thread {

    private final BinaryLogClient client;

    public MysqlBinLog(String host, int port, String userName, String pwd) {
        Instance.redisTable();
        client = new BinaryLogClient(host, port, userName, pwd);
        EventDeserializer eventDeserializer = new EventDeserializer();
        client.setEventDeserializer(eventDeserializer);
    }

    public MysqlBinLog registerEvent(BinaryLogClient.EventListener eventListener) {
        if (eventListener != null) {
            client.registerEventListener(eventListener);
        }
        return this;
    }

    @Override
    public void run() {
        try {
            client.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
