package core.group;


import core.msg.TransferMsg;

/**
 * Created by Administrator on 2020/6/19.
 */
public interface EventHandler {
    void onEvent(TransferMsg transferMsg);
}
