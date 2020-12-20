package core.groupHelper;


import core.proto.TransferMsg;

/**
 * Created by Administrator on 2020/6/19.
 */
public interface EventHandler {

    public void onEvent(TransferMsg transferMsg);
}
