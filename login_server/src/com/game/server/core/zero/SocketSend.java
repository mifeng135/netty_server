package com.game.server.core.zero;

import com.game.server.core.msg.MsgBean;
import io.netty.buffer.ByteBuf;
import nanomsg.pipeline.PushSocket;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Administrator on 2020/6/12.
 */
public abstract class SocketSend extends Thread {

    private PushSocket mSocket;

    private final ConcurrentLinkedQueue<MsgBean> mMsgQueue = new ConcurrentLinkedQueue();

    public SocketSend() {
        mSocket = new PushSocket();
        mSocket.setSendTimeout(30000);
        mSocket.bind(getSocketIp());
    }

    protected abstract String getSocketIp();

    public void pushSendMsg(MsgBean data) {
        mMsgQueue.offer(data);
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

            }
        }
    }
}
