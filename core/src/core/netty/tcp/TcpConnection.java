package core.netty.tcp;

import core.Constants;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

public class TcpConnection {

    private Bootstrap bootstrap;
    private EventLoopGroup workerGroup;
    private TcpConnectionHandler handler;
    private final Integer key;

    public TcpConnection(Integer key, TcpConnectionHandler handler) {
        this.handler = handler;
        this.key = key;
        createConnection();
    }

    public TcpConnection(Integer key) {
        this.handler = new TcpConnectionHandler();
        this.key = key;
        createConnection();
    }


    public void createConnection() {
        workerGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        workerGroup.setIoRatio(Constants.NETTY_IO_RATIO_DEFAULT);
        bootstrap = new Bootstrap();
        bootstrap.group(workerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel channel) throws Exception {
                ChannelPipeline pipeline = channel.pipeline();
                pipeline.addLast(new CDecoder());
                pipeline.addLast(new CEncode());
                pipeline.addLast("clientHandler", handler);
            }
        });
    }

    public void connect(String ip, int port) {
        ChannelFuture future = bootstrap.connect(new InetSocketAddress(ip, port));
        future.awaitUninterruptibly();
        future.channel().attr(Constants.PLAYER_INDEX).setIfAbsent(key);
        future.channel().attr(Constants.CONNECT_IP).setIfAbsent(ip);
        future.channel().attr(Constants.PORT).setIfAbsent(port);
        future.channel().attr(Constants.TCP).setIfAbsent(this);
        future.addListener(new ConnectionListener());
    }

    public void doStop() {
        workerGroup.shutdownGracefully();
    }
}
