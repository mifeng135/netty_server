package core.socket;


import core.msg.TransferMsg;

/**
 * Created by Administrator on 2020/7/28.
 */
public interface MSocketAdapter {
    public int getSectionId(TransferMsg msg);

    public String getRegionString(TransferMsg msg);
}
