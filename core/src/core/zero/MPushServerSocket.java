package core.zero;

import core.proto.TransferMsg;
import nanomsg.pipeline.PushSocket;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Administrator on 2020/7/28.
 * Pipeline server
 */
public class MPushServerSocket extends Thread {

    private final PushSocket mSocket;
    private final String mServerKey;

    private ConcurrentLinkedQueue<TransferMsg> mMsgQueue = new ConcurrentLinkedQueue();

    public MPushServerSocket(String ip, int port, String serverKey) {
        mSocket = new PushSocket();
        mServerKey = serverKey;
        mSocket.bind("tcp://" + ip + ":" + port);
    }

    public void pushSendMsg(TransferMsg msg) {
        msg.setSocketKey(mServerKey);
        mMsgQueue.offer(msg);
    }

    @Override
    public void run() {
        while (true) {
            try {
                for (int i = 0; i < 100; i++) {
                    TransferMsg msg = mMsgQueue.poll();
                    if (msg == null) {
                        break;
                    }
                    mSocket.send(msg.packServerMsg());
                }
                Thread.sleep(10);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
