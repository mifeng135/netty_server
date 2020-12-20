package core.zero;

import core.proto.TransferMsg;
import nanomsg.bus.BusSocket;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * create broadcast client
 * Created by Administrator on 2020/12/13.
 */
public class MBusClientSocket extends Thread {

    private final BusSocket mSocket;

    private ConcurrentLinkedQueue<TransferMsg> mMsgQueue = new ConcurrentLinkedQueue();

    public MBusClientSocket(String ip, int port) {
        mSocket = new BusSocket();
        mSocket.bind("tcp://" + ip + ":" + port);
    }

    public void pushSendMsg(TransferMsg msg) {
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
