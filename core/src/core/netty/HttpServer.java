package core.netty;

import core.Configs;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import org.apache.log4j.Logger;

import java.net.InetSocketAddress;

/**
 * Created by Administrator on 2020/6/23.
 */
public class HttpServer {

    private static Logger logger = Logger.getLogger(HttpServer.class);

    private ServerBootstrap mServerBootstrap;
    private EventLoopGroup mEventLoopGroup;
    private ChannelFuture mChannelFuture;

    private final String mIp;
    private final int mPort;
    private HttpHandler httpHandler;

    public HttpServer(String ip, int port, HttpHandler handler) {
        mIp = ip;
        mPort = port;
        httpHandler = handler;
        doInitNetty();
    }

    public void doInitNetty() {
        mEventLoopGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        workerGroup.setIoRatio(Configs.NETTY_IO_RATIO_DEFAULT);
        mServerBootstrap = new ServerBootstrap();
        mServerBootstrap.group(mEventLoopGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, Configs.TCP_SO_BACKLOG_DEFAULT)
                .option(ChannelOption.SO_REUSEADDR, Configs.TCP_SO_REUSEADDR_DEFAULT)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childOption(ChannelOption.TCP_NODELAY, Configs.TCP_NO_DELAY_DEFAULT)
                .childOption(ChannelOption.SO_KEEPALIVE, Configs.TCP_SO_KEEP_ALIVE_DEFAULT)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

        mServerBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel channel) throws Exception {
                ChannelPipeline pipeline = channel.pipeline();
                pipeline.addLast(new HttpContentCompressor());
                pipeline.addLast(new HttpServerCodec());
                pipeline.addLast(new HttpObjectAggregator(65536));
                pipeline.addLast("httpHandler", httpHandler);
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
                logger.error("login server start success");
                System.out.print("login server start success");
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
