package com.game.server.core.zero;

import com.game.server.core.msg.MsgBean;
import io.netty.buffer.ByteBuf;
import nanomsg.pipeline.PushSocket;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Administrator on 2020/7/28.
 */
public class Send extends Thread {

    private final PushSocket mSocket;
    private final int serverKey;

    private ConcurrentLinkedQueue<MsgBean> mMsgQueue = new ConcurrentLinkedQueue();


    public Send(String ip, int port, int serverKey) {
        mSocket = new PushSocket();
        mSocket.setSendTimeout(30000);
        mSocket.bind(ip + ":" + port);
        this.serverKey = serverKey;
        setDaemon(true);
    }

    public void pushSendMsg(MsgBean buf) {
        mMsgQueue.offer(buf);
    }

    public int getServerKey() {
        return serverKey;
    }

    @Override
    public void run() {
        while (true) {
            try {
                for (int i = 0; i < 100; i++) {
                    MsgBean msgBean = mMsgQueue.poll();
                    if (msgBean == null) {
                        break;
                    }
                    ByteBuf buf = msgBean.toByteBuf();
                    mSocket.send(buf.array());
                    buf.release();
                }
                Thread.sleep(10);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
