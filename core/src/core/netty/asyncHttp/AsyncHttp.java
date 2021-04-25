package core.netty.asyncHttp;

import core.msg.TransferMsg;
import core.util.ProtoUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.AsyncHttpClient;

import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import protocal.TcpHeaderProto;

import java.util.concurrent.ExecutionException;

import static org.asynchttpclient.Dsl.asyncHttpClient;

public class AsyncHttp {

    private AsyncHttpClient asyncHttpClient;
    private String baseUrl;

    private static class DefaultInstance {
        static final AsyncHttp INSTANCE = new AsyncHttp();
    }

    public static AsyncHttp getInstance() {
        return DefaultInstance.INSTANCE;
    }

    private AsyncHttp() {
        asyncHttpClient = asyncHttpClient(new DefaultAsyncHttpClientConfig.Builder()
                .setFollowRedirect(true)
                .setIoThreadsCount(Runtime.getRuntime().availableProcessors() * 2)
                .setConnectTimeout(1000)
                .setReadTimeout(1000)
                .setRequestTimeout(3000)
                .setMaxRequestRetry(2)
                .setThreadPoolName("AsyncHttp"));
    }

    public void initBaseUrl(String url) {
        baseUrl = url;
    }

    /**
     * 异步请求
     *
     * @param msg
     * @param handler
     */
    public void postAsync(TcpHeaderProto headerDBProto, Object msg, AsyncCompletionHandler handler) {
        byte[] headerData = ProtoUtil.serialize(headerDBProto);
        byte[] data = ProtoUtil.serialize(msg);
        ByteBuf byteBuf = Unpooled.buffer(4 + data.length + headerData.length);
        byteBuf.writeShort(headerData.length);
        byteBuf.writeShort(data.length);
        byteBuf.writeBytes(headerData);
        byteBuf.writeBytes(data);
        asyncHttpClient.preparePost(baseUrl).setBody(byteBuf.array()).execute(handler);
    }

    /**
     * 异步请求
     *
     * @param data
     * @param handler
     */
    public void postAsync(TcpHeaderProto headerDBProto, byte[] data, AsyncCompletionHandler handler) {
        byte[] headerData = ProtoUtil.serialize(headerDBProto);
        ByteBuf byteBuf = Unpooled.buffer(4 + data.length + headerData.length);
        byteBuf.writeShort(headerData.length);
        byteBuf.writeShort(data.length);
        byteBuf.writeBytes(headerData);
        byteBuf.writeBytes(data);
        asyncHttpClient.preparePost(baseUrl).setBody(byteBuf.array()).execute(handler);
    }

    /**
     * 同步请求
     *
     * @param headerDBProto
     * @param msg
     */
    public TransferMsg postSync(TcpHeaderProto headerDBProto, Object msg) {
        byte[] headerData = ProtoUtil.serialize(headerDBProto);
        byte[] data = ProtoUtil.serialize(msg);
        ByteBuf byteBuf = Unpooled.buffer(4 + data.length + headerData.length);
        byteBuf.writeShort(headerData.length);
        byteBuf.writeShort(data.length);
        byteBuf.writeBytes(headerData);
        byteBuf.writeBytes(data);
        TransferMsg transferMsg = null;
        try {
            byte[] responseData = asyncHttpClient.preparePost(baseUrl).setBody(byteBuf.array()).execute().get().getResponseBodyAsBytes();
            transferMsg = transferData(responseData);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return transferMsg;
    }


    /**
     * 同步请求
     *
     * @param headerDBProto
     * @param data
     */
    public TransferMsg postSync(TcpHeaderProto headerDBProto, byte[] data) {
        byte[] headerData = ProtoUtil.serialize(headerDBProto);
        ByteBuf byteBuf = Unpooled.buffer(4 + data.length + headerData.length);
        byteBuf.writeShort(headerData.length);
        byteBuf.writeShort(data.length);
        byteBuf.writeBytes(headerData);
        byteBuf.writeBytes(data);
        TransferMsg transferMsg = null;
        try {
            byte[] responseData = asyncHttpClient.preparePost(baseUrl).setBody(byteBuf.array()).execute().get().getResponseBodyAsBytes();
            transferMsg = transferData(responseData);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return transferMsg;
    }

    public TransferMsg transferData(byte[] data) {

        ByteBuf buf = Unpooled.wrappedBuffer(data);

        int headerLen = buf.readShort();
        int bodyLen = buf.readShort();

        byte[] headerData = new byte[headerLen];
        buf.readBytes(headerData);

        byte[] bodyData = new byte[bodyLen];
        buf.readBytes(bodyData);

        TcpHeaderProto headerProto = ProtoUtil.deserializer(headerData, TcpHeaderProto.class);
        TransferMsg transferMsg = new TransferMsg();
        transferMsg.setHeaderProto(headerProto);
        transferMsg.setData(bodyData);
        return transferMsg;
    }
}
