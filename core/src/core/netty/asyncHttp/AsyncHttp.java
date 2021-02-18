package core.netty.asyncHttp;

import core.annotation.CtrlAnnotation;
import core.util.ProtoUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.AsyncHttpClient;

import java.util.concurrent.ExecutionException;

import static org.asynchttpclient.Dsl.asyncHttpClient;

public class AsyncHttp {

    private AsyncHttpClient asyncHttpClient;

    private static class DefaultInstance {
        static final AsyncHttp INSTANCE = new AsyncHttp();
    }

    public static AsyncHttp getInstance() {
        return DefaultInstance.INSTANCE;
    }

    private AsyncHttp() {
        asyncHttpClient = asyncHttpClient();
    }
    /**
     * 异步请求
     *
     * @param url
     * @param msgId
     * @param msg
     * @param handler
     */
    public void postAsync(String url, int msgId, Object msg, AsyncCompletionHandler handler) {
        byte[] data = ProtoUtil.serialize(msg);
        ByteBuf byteBuf = Unpooled.buffer(4 + data.length);
        byteBuf.writeInt(msgId);
        byteBuf.writeBytes(data);
        asyncHttpClient.preparePost(url).setBody(byteBuf.array()).execute(handler);
    }

    /**
     * 同步请求
     *
     * @param url
     * @param msgId
     * @param msg
     */
    public byte[] postSync(String url, int msgId, Object msg) {
        byte[] data = ProtoUtil.serialize(msg);
        ByteBuf byteBuf = Unpooled.buffer(4 + data.length);
        byteBuf.writeInt(msgId);
        byteBuf.writeBytes(data);
        try {
            return asyncHttpClient.preparePost(url).setBody(byteBuf.array()).execute().get().getResponseBodyAsBytes();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
