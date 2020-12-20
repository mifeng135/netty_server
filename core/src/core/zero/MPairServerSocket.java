package core.zero;

import core.groupHelper.MessageDispatchRegion;
import core.groupHelper.MessageGroup;
import core.proto.ProtoUtil;
import core.proto.TransferMsg;
import nanomsg.pair.PairSocket;

/**
 * Created by Administrator on 2020/12/13.
 */
public class MPairServerSocket extends Thread implements MPairSocket {

    private final PairSocket mSocket;
    private final MSocketAdapter mSocketAdapter;
    private final String mSocketKey;

    public MPairServerSocket(String ip, int port,String socketKey, MSocketAdapter adapter) {
        mSocket = new PairSocket();
        mSocket.bind("tcp://" + ip + ":" + port);
        mSocketAdapter = adapter;
        mSocketKey = socketKey;
    }

    public void sendMsg(TransferMsg msg) {
        msg.setSocketKey(mSocketKey);
        mSocket.send(msg.packServerMsg());
    }

    @Override
    public void run() {
        while (true) {
            try {
                dispatch(mSocket.recvBytes());
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
