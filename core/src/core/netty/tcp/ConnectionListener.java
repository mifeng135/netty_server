package core.netty.tcp;

import core.Constants;
import core.manager.LocalSocketManager;
import core.manager.SocketManager;
import core.util.SocketUtil;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protocol.system.TcpReq;

import java.util.concurrent.TimeUnit;

import static protocol.MsgConstant.MSG_TCP_REQ;

public class ConnectionListener implements ChannelFutureListener {

    private static Logger logger = LoggerFactory.getLogger(ConnectionListener.class);

    @Override
    public void operationComplete(ChannelFuture channelFuture) {
        if (!channelFuture.isSuccess()) {
            final EventLoop loop = channelFuture.channel().eventLoop();
            loop.schedule(new Runnable() {
                @Override
                public void run() {
                    TcpConnection connection = channelFuture.channel().attr(Constants.TCP).get();
                    String ip = channelFuture.channel().attr(Constants.CONNECT_IP).get();
                    int port = channelFuture.channel().attr(Constants.PORT).get();
                    connection.connect(ip, port);
                }
            }, 10L, TimeUnit.SECONDS);
        } else {
            int socketIndex = channelFuture.channel().attr(Constants.SOCKET_INDEX).get();
            String ip = channelFuture.channel().attr(Constants.CONNECT_IP).get();
            int port = channelFuture.channel().attr(Constants.PORT).get();
            logger.info("connect success ip = {} port = {}", ip,port);
            LocalSocketManager.getInstance().putChannel(socketIndex, channelFuture.channel());
            TcpReq tcpReq = new TcpReq();
            tcpReq.setSocketIndex(socketIndex);
            SocketUtil.sendLoaclTcpMsgToServer(socketIndex, MSG_TCP_REQ, tcpReq);
        }
    }
}
