package core.util;

import core.manager.HttpConnectManager;
import core.manager.WebSocketConnectionManager;
import core.proto.TransferMsg;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

/**
 * Created by Administrator on 2020/12/6.
 */
public class SocketUtil {

    /**
     * send http msg
     * @param playerIndex
     * @param msgId
     * @param data
     */
    public static void sendHttpMsg(int playerIndex, int msgId, byte[] data) {

        Channel channel = HttpConnectManager.removeConnect(playerIndex);

        if(channel == null || !channel.isActive()) {
            return;
        }
        ByteBuf buf = Unpooled.buffer(data.length + 8);
        buf.writeInt(msgId);
        buf.writeInt(data.length);
        buf.writeBytes(data);

        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        channel.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * send webSocket msg
     * @param playerIndex
     * @param msgId
     * @param data
     */
    public static void sendWebSocketMsg(int playerIndex, int msgId, byte[] data) {
        ByteBuf buf = Unpooled.buffer(data.length + 8);
        buf.writeInt(msgId);
        buf.writeInt(data.length);
        buf.writeBytes(data);

        Channel channel = WebSocketConnectionManager.getChannelByPlayerIndex(playerIndex);
        if (channel != null && channel.isActive()) {
            BinaryWebSocketFrame webSocketFrame = new BinaryWebSocketFrame(buf);
            channel.writeAndFlush(webSocketFrame);
        }
    }


    /**
     * send webSocket msg
     * @param transferMsg
     */
    public static void sendWebSocketMsg(TransferMsg transferMsg) {
        ByteBuf buf = transferMsg.packClientMsg();
        Channel channel = WebSocketConnectionManager.getChannelByPlayerIndex(transferMsg.getPlayerIndex());
        if (channel != null && channel.isActive()) {
            BinaryWebSocketFrame webSocketFrame = new BinaryWebSocketFrame(buf);
            channel.writeAndFlush(webSocketFrame);
        }
    }
}
