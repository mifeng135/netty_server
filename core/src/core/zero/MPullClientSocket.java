package core.zero;

import core.groupHelper.MessageDispatchRegion;
import core.groupHelper.MessageGroup;
import core.proto.ProtoUtil;
import core.proto.TransferMsg;
import nanomsg.pipeline.PullSocket;
import org.apache.log4j.Logger;

/**
 * Created by Administrator on 2020/7/28.
 *
 * Pipeline client
 *
 */



public class MPullClientSocket extends Thread {

    private static Logger logger = Logger.getLogger(MPullClientSocket.class);

    private final PullSocket mSocket;
    private final MSocketAdapter mSocketAdapter;

    public MPullClientSocket(String ip, int port, MSocketAdapter adapter) {
        mSocket = new PullSocket();
        mSocket.connect("tcp://" + ip + ":" + port);
        mSocketAdapter = adapter;
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
