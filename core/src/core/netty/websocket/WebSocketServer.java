package core.netty.websocket;

import core.Constants;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * Created by Administrator on 2020/6/24.
 */
public abstract class WebSocketServer {

    private static Logger logger = LoggerFactory.getLogger(WebSocketServer.class);

    private ServerBootstrap mServerBootstrap;
    private EventLoopGroup mEventLoopGroup;
    private ChannelFuture mChannelFuture;

    private final String mIp;
    private final int mPort;
    private final WebSocketHandler webSocketHandler;

    public WebSocketServer(String ip, int port, WebSocketHandler handler) {
        mIp = ip;
        mPort = port;
        webSocketHandler = handler;
        doInitNetty();
    }

    public void doInitNetty() {
        mEventLoopGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        workerGroup.setIoRatio(Constants.NETTY_IO_RATIO_DEFAULT);
        mServerBootstrap = new ServerBootstrap();
        mServerBootstrap.group(mEventLoopGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, Constants.TCP_SO_BACKLOG_DEFAULT)
                .option(ChannelOption.SO_REUSEADDR, Constants.TCP_SO_REUSEADDR_DEFAULT)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childOption(ChannelOption.TCP_NODELAY, Constants.TCP_NO_DELAY_DEFAULT)
                .childOption(ChannelOption.SO_KEEPALIVE, Constants.TCP_SO_KEEP_ALIVE_DEFAULT)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

        mServerBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel channel) throws Exception {
                ChannelPipeline pipeline = channel.pipeline();
                if (Constants.NETTY_OPEN_IDLE) {
                    pipeline.addLast("idleStateHandler", new IdleStateHandler(Constants.TCP_SERVER_IDLE_DEFAULT, 0, 0));
                }
                pipeline.addLast(new HttpServerCodec());
                pipeline.addLast(new HttpObjectAggregator(65536));
                pipeline.addLast("serverIdleHandler", getServerHandler());
            }
        });

        doStart();
    }

    /**
     * start game server
     */
    private void doStart() {
        try {
            ChannelFuture channelFuture = mChannelFuture = mServerBootstrap.bind(new InetSocketAddress(mIp, mPort)).sync();
            if (channelFuture.isSuccess()) {
                logger.error("gate server start success");
            }
        } catch (InterruptedException e) {
            doStop();
            e.printStackTrace();
        }
    }

    public void doStop() {
        mChannelFuture.channel().close();
        mEventLoopGroup.shutdownGracefully().awaitUninterruptibly();
    }

    public abstract WebSocketHandler getServerHandler();
}
