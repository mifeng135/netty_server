package core.zero;

import core.groupHelper.MessageDispatchRegion;
import core.groupHelper.MessageGroup;
import core.proto.ProtoUtil;
import core.proto.TransferMsg;
import nanomsg.bus.BusSocket;
import org.apache.log4j.Logger;


/**
 * create broadcast server
 * Created by Administrator on 2020/12/13.
 */
public class MBusServerSocket extends Thread {

    private static Logger logger = Logger.getLogger(MBusServerSocket.class);

    private final BusSocket mSocket;
    private final MSocketAdapter mSocketAdapter;


    public MBusServerSocket(String ip, int port, MSocketAdapter adapter) {
        mSocket = new BusSocket();
        mSocket.bind("tcp://" + ip + ":" + port);
        mSocketAdapter = adapter;
    }

    public void pushSendMsg(TransferMsg msg) {
        mSocket.send(msg.packServerMsg());
    }

    @Override
    public void run() {
        while (true) {
            try {
                byte[] data = mSocket.recvBytes();
                dispatch(data);
            } catch (Exception e) {

            }
        }
    }

    private void dispatch(byte[] data) {
        TransferMsg transferMsg = ProtoUtil.deserializer(data, TransferMsg.class);
        String regionString = mSocketAdapter.getRegionString(transferMsg);
        if (regionString != null) {
            MessageGroup messageGroup = MessageDispatchRegion.getInstance().getMessageGroupByTag(regionString);
            String prefix = messageGroup.getPrefix();
            int count = messageGroup.getCount();
            String section = prefix + mSocketAdapter.getSectionId(transferMsg) % count;
            messageGroup.pushMessageWithTag(section, transferMsg);
        }
    }
}
