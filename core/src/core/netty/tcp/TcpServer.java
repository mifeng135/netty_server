package core.netty.tcp;

import core.Constants;
import core.netty.websocket.WebSocketServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

import static core.Constants.IDLE_STATE_HANDLER;

/**
 * Created by Administrator on 2020/12/19.
 */
public class TcpServer {

    private static Logger logger = LoggerFactory.getLogger(TcpServer.class);
    private final String mIp;
    private final int mPort;

    private ServerBootstrap mServerBootstrap;
    private EventLoopGroup mEventLoopGroup;
    private ChannelFuture mChannelFuture;
    private TcpServerHandler tcpServerHandler;

    public TcpServer(String ip, int port, TcpServerHandler tcpServerHandler) {
        mIp = ip;
        mPort = port;
        this.tcpServerHandler = tcpServerHandler;
        doInitNetty();
    }

    public TcpServer(String ip, int port) {
        mIp = ip;
        mPort = port;
        this.tcpServerHandler = new TcpServerHandler();
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
                    pipeline.addLast(IDLE_STATE_HANDLER, new IdleStateHandler(Constants.TCP_SERVER_IDLE_DEFAULT, 0, 0));
                }
                pipeline.addLast(new MDecoder());
                pipeline.addLast(new MEncoder());
                pipeline.addLast("serverHandler", tcpServerHandler);
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
                logger.error("server start success");
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
}
