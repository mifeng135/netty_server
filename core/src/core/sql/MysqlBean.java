package core.sql;

/**
 * Created by Administrator on 2020/6/15.
 */
public class MysqlBean {

    private int cmd;
    private Object data;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }
}
