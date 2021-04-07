package core.netty.tcp;

import core.Constants;
import core.manager.LocalSocketManager;
import core.msg.TransferMsg;
import core.util.ProtoUtil;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protocal.local.base.HeaderProto;

import java.util.concurrent.TimeUnit;

import static protocal.MsgConstant.MSG_LOCAL_SOCKET_REQ;

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
            int playerIndex = channelFuture.channel().attr(Constants.PLAYER_INDEX).get();
            String ip = channelFuture.channel().attr(Constants.CONNECT_IP).get();
            int port = channelFuture.channel().attr(Constants.PORT).get();
            logger.info("connect success ip = {} port = {} playerIndex = {}", ip, port, playerIndex);
            LocalSocketManager.getInstance().putChannel(playerIndex, channelFuture.channel());

            HeaderProto headerProto = ProtoUtil.initHeaderProto(MSG_LOCAL_SOCKET_REQ, playerIndex);
            TransferMsg transferMsg = new TransferMsg();
            transferMsg.setHeaderProto(headerProto);
            channelFuture.channel().writeAndFlush(transferMsg);
        }
    }
}
