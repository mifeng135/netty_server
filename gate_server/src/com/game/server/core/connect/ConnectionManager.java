package com.game.server.core.connect;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import io.netty.util.internal.PlatformDependent;

import java.util.concurrent.ConcurrentMap;

/**
 * Created by Administrator on 2020/6/8.
 */
public class ConnectionManager {

    public static final ChannelGroup mClientChannelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    public static final ConcurrentMap<Integer, ChannelId> mfdChannelGroup = PlatformDependent.newConcurrentHashMap();


    public static int getConnectSize() {
        return mfdChannelGroup.size();
    }

    public static Channel getChannelById(int id) {
        ChannelId channelId = mfdChannelGroup.get(id);
        if (channelId != null) {
            return mClientChannelGroup.find(channelId);
        }
        return null;
    }

    public static void send2AllClient(ByteBuf buf) {
        mClientChannelGroup.writeAndFlush(buf);
    }

    public static void send2ClientByFD(int id, ByteBuf buf) {
        Channel channel = getChannelById(id);
        if (channel != null && channel.isActive()) {
            BinaryWebSocketFrame webSocketFrame = new BinaryWebSocketFrame(buf);
            channel.writeAndFlush(webSocketFrame);
        }
    }

    public static void send2Client(Channel channel, ByteBuf buf) {
        if (channel == null || channel.isActive() == false) {
            return;
        }
        BinaryWebSocketFrame webSocketFrame = new BinaryWebSocketFrame(buf);
        channel.writeAndFlush(webSocketFrame);
    }
}
