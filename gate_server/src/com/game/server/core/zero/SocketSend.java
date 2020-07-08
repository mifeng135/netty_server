package com.game.server.core.zero;

import com.game.server.core.msg.MsgBean;
import io.netty.buffer.ByteBuf;
import nanomsg.pipeline.PushSocket;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Administrator on 2020/6/12.
 */
public abstract class SocketSend extends Thread {

    private final PushSocket mSocket;
    private ConcurrentLinkedQueue<MsgBean> mMsgQueue = new ConcurrentLinkedQueue();

    public SocketSend() {
        mSocket = new PushSocket();
        mSocket.setSendTimeout(30000);
        mSocket.bind(getSocketIp());
        setDaemon(true);
    }

    public void pushSendMsg(MsgBean buf) {
        mMsgQueue.offer(buf);
    }

    protected abstract String getSocketIp();

    @Override
    public void run() {
        while (true) {
            try {
                MsgBean msgBean = mMsgQueue.poll();
                if (msgBean == null) {
                    Thread.sleep(100);
                    continue;
                }
                ByteBuf buf = msgBean.toByteBuf();
                mSocket.send(buf.array());
                buf.release();
            } catch (Exception e) {

            }
        }
    }
}
