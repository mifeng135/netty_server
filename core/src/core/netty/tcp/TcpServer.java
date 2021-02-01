package core.netty.tcp;

import core.Constants;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

import static core.Constants.IDLE_STATE_HANDLER;
import static core.Constants.LOCAL;
import static core.Constants.REMOTE;

/**
 * Created by Administrator on 2020/12/19.
 */
public class TcpServer {

    private final String mIp;
    private final int mPort;
    private final int socketType;

    private ServerBootstrap mServerBootstrap;
    private EventLoopGroup mEventLoopGroup;
    private ChannelFuture mChannelFuture;
    private TcpServerHandler tcpServerHandler;
    private GenericFutureListener channelFutureListener;

    public TcpServer(String ip, int port, int socketType, TcpServerHandler tcpServerHandler) {
        mIp = ip;
        mPort = port;
        this.socketType = socketType;
        this.tcpServerHandler = tcpServerHandler;
        init();
    }

    public TcpServer(String ip, int port, int socketType) {
        mIp = ip;
        mPort = port;
        this.tcpServerHandler = new TcpServerHandler();
        this.socketType = socketType;
        init();
    }

    public void init() {
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
                if (socketType == LOCAL) {
                    pipeline.addLast(new SDecoder());
                    pipeline.addLast(new SEncode());
                } else if (socketType == REMOTE) {
                    pipeline.addLast(new GDecoder());
                    pipeline.addLast(new GEncoder());
                }
                pipeline.addLast("serverHandler", tcpServerHandler);
            }
        });
    }

    public void setStartListener(ChannelFutureListener listener) {
        channelFutureListener = listener;
    }

    /**
     * start game server
     */
    public void startServer() {
        try {
            ChannelFuture channelFuture = mChannelFuture = mServerBootstrap.bind(new InetSocketAddress(mIp, mPort)).sync();
            if (channelFutureListener != null) {
                channelFuture.addListener(channelFutureListener);
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
