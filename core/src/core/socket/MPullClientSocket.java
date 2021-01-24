package core.socket;

import core.group.MessageGroup;
import core.util.ProtoUtil;
import core.msg.TransferMsg;
import nanomsg.pipeline.PullSocket;

/**
 * Created by Administrator on 2020/7/28.
 * <p>
 * Pipeline client
 */


public class MPullClientSocket extends Thread {


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
        MessageGroup.getInstance().pushMessage(transferMsg);
    }
}
