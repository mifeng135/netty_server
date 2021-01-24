package core.util;

import core.manager.HttpConnectManager;
import core.manager.LocalSocketManager;
import core.manager.SocketManager;
import core.manager.WebSocketConnectionManager;
import core.msg.TransferMsg;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

import java.util.List;

/**
 * Created by Administrator on 2020/12/6.
 */
public class SocketUtil {

    /**
     * send http msg
     *
     * @param playerIndex
     * @param msgId
     * @param msg
     */
    public static void sendHttpMsg(int playerIndex, int msgId, int result, Object msg) {
        byte[] data = ProtoUtil.serialize(msg);
        Channel channel = HttpConnectManager.removeConnect(playerIndex);

        if (channel == null || !channel.isActive()) {
            return;
        }
        ByteBuf buf = Unpooled.buffer(data.length + 6);
        buf.writeInt(msgId);
        buf.writeShort(result);
        buf.writeBytes(data);

        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        channel.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * server send msg to remote connection
     *
     * @param socketIndex
     * @param msgId
     * @param result
     * @param msg
     */
    public static void sendRemoteTcpMsgToConnection(int socketIndex, int msgId, int result, Object msg) {
        byte[] data = ProtoUtil.serialize(msg);
        TransferMsg transferMsg = new TransferMsg();
        transferMsg.setMsgId(msgId);
        transferMsg.setData(data);
        transferMsg.setResult(result);
        Channel channel = SocketManager.getInstance().getChanel(socketIndex);
        if (channel != null && channel.isActive()) {
            channel.writeAndFlush(transferMsg);
        }
    }

    public static void sendRemoteAllTcpMsg(int msgId, Object msg) {
        byte[] data = ProtoUtil.serialize(msg);
        TransferMsg transferMsg = new TransferMsg();
        transferMsg.setMsgId(msgId);
        transferMsg.setData(data);
        transferMsg.setResult(0);
        SocketManager.getInstance().broadcast(transferMsg);
    }

    public static void sendRemoteTcpMsgWithList(List<Integer> list, int msgId, Object msg) {
        byte[] data = ProtoUtil.serialize(msg);
        TransferMsg transferMsg = new TransferMsg();
        transferMsg.setMsgId(msgId);
        transferMsg.setData(data);
        transferMsg.setResult(0);

        for (int i = 0; i < list.size(); i++) {
            int socketIndex = list.get(i);
            Channel channel = SocketManager.getInstance().getChanel(socketIndex);
            if (channel != null && channel.isActive()) {
                channel.writeAndFlush(transferMsg);
            }
        }
    }

    /**
     * connection send msg to server
     *
     * @param socketIndex
     * @param msgId
     * @param msg
     */
    public static void sendLoaclTcpMsgToServer(int socketIndex, int msgId, Object msg) {
        byte[] data = ProtoUtil.serialize(msg);

        TransferMsg transferMsg = new TransferMsg();
        transferMsg.setSocketIndex(socketIndex);
        transferMsg.setMsgId(msgId);
        transferMsg.setData(data);

        Channel channel = LocalSocketManager.getInstance().getChanel(socketIndex);
        if (channel != null && channel.isActive()) {
            channel.writeAndFlush(transferMsg);
        }
    }


    /**
     * server send msg to connection
     *
     * @param socketIndex
     * @param msgId
     * @param result
     * @param msg
     */
    public static void sendLocalTcpMsgToConnection(int socketIndex, int msgId, int result, Object msg) {
        byte[] data = ProtoUtil.serialize(msg);
        TransferMsg transferMsg = new TransferMsg();
        transferMsg.setMsgId(msgId);
        transferMsg.setData(data);
        transferMsg.setResult(result);
        Channel channel = LocalSocketManager.getInstance().getChanel(socketIndex);
        if (channel != null && channel.isActive()) {
            channel.writeAndFlush(transferMsg);
        }
    }

    public static void sendLocalAllTcpMsg(int msgId, Object msg) {
        byte[] data = ProtoUtil.serialize(msg);
        TransferMsg transferMsg = new TransferMsg();
        transferMsg.setMsgId(msgId);
        transferMsg.setData(data);
        transferMsg.setResult(0);
        LocalSocketManager.getInstance().broadcast(transferMsg);
    }
}
