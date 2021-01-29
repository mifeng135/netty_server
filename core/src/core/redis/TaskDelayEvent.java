package core.redis;

import java.io.Serializable;

public class TaskDelayEvent implements Serializable {
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
