package com.game.game.core.netty;

import com.game.game.core.config.Configs;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.timeout.IdleStateHandler;
import org.apache.log4j.Logger;

import java.net.InetSocketAddress;

/**
 * Created by Administrator on 2020/6/24.
 */
public abstract class NettyServer {

    private static Logger logger = Logger.getLogger(NettyServer.class);

    private ServerBootstrap mServerBootstrap;
    private EventLoopGroup mEventLoopGroup;
    private ChannelFuture mChannelFuture;


    private final String mIp;
    private final int mPort;

    public NettyServer(String ip, int port) {
        mIp = ip;
        mPort = port;
        init();
    }
    public void doInitNetty() {
        mEventLoopGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2);
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

        doInitWriteBufferWaterMark();
        mServerBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel channel) throws Exception {
                ChannelPipeline pipeline = channel.pipeline();
                if (Configs.NETTY_OPEN_IDLE) {
                    pipeline.addLast("idleStateHandler", new IdleStateHandler(Configs.TCP_SERVER_IDLE_DEFAULT, 0, 0));
                }
                pipeline.addLast(new HttpServerCodec());
                pipeline.addLast(new HttpObjectAggregator(65536));
                pipeline.addLast("serverIdleHandler", getServerHandler());
            }
        });

        doStart();
    }

    private void doInitWriteBufferWaterMark() {
        if (Configs.NETTY_BUFFER_HIGH_WATERMARK_DEFAULT > Configs.NETTY_BUFFER_LOW_WATERMARK_DEFAULT) {
            mServerBootstrap.childOption(ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK, Configs.NETTY_BUFFER_HIGH_WATERMARK_DEFAULT)
                    .childOption(ChannelOption.WRITE_BUFFER_LOW_WATER_MARK, Configs.NETTY_BUFFER_LOW_WATERMARK_DEFAULT);
        }
    }

    /**
     * start game server
     */
    private void doStart() {
        try {
            ChannelFuture channelFuture = mChannelFuture = mServerBootstrap.bind(new InetSocketAddress(mIp, mPort)).sync();
            if (channelFuture.isSuccess()) {
                logger.error("game start success");
                System.out.println("game start success");
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

    public abstract void init();
    public abstract ServerHandler getServerHandler();

}
