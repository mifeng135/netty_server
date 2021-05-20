package core.netty.http;


import core.Constants;
import core.group.MessageGroup;
import core.msg.HeaderProto;
import core.msg.TransferMsg;
import core.util.ProtoUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static core.Constants.HTTP_DECODER_TYPE_JSON;
import static core.Constants.HTTP_DECODER_TYPE_PROTO_BUFFER;

/**
 * Created by Administrator on 2020/6/23.
 */

@ChannelHandler.Sharable
public class HttpHandler extends ChannelInboundHandlerAdapter {

    private static AtomicInteger atomicInteger = new AtomicInteger(0);
    private int type = HTTP_DECODER_TYPE_PROTO_BUFFER;

    public HttpHandler(int encodeType) {
        type = encodeType;
    }

    public HttpHandler() {

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (!ctx.channel().isActive()) {
            return;
        }
        int httpIndex = atomicInteger.incrementAndGet();
        if (httpIndex >= Integer.MAX_VALUE) {
            atomicInteger.compareAndSet(Integer.MAX_VALUE, 0);
        }
        FullHttpRequest request = (FullHttpRequest) msg;
        try {
            HttpMethod method = request.method();
            if (HttpMethod.POST.equals(method) || HttpMethod.GET.equals(method)) {
                if (type == HTTP_DECODER_TYPE_PROTO_BUFFER) {
                    processProtoBuffer(request, httpIndex, ctx);
                }
                if (type == HTTP_DECODER_TYPE_JSON) {
                    processJson(request, httpIndex, ctx);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processProtoBuffer(FullHttpRequest request, int httpIndex, ChannelHandlerContext ctx) {
        ByteBuf buf = request.content();

        int headerLen = buf.readShort();
        int bodyLen = buf.readShort();

        byte[] headerData = new byte[headerLen];
        buf.readBytes(headerData);

        byte[] bodyData = new byte[bodyLen];
        buf.readBytes(bodyData);

        HeaderProto headerProto = ProtoUtil.deserializer(headerData, HeaderProto.class);
        int playerIndex = headerProto.getPlayerIndex() == 0 ? httpIndex : headerProto.getPlayerIndex();
        headerProto.setPlayerIndex(playerIndex);
        TransferMsg transferMsg = new TransferMsg();
        transferMsg.setHeaderProto(headerProto);
        transferMsg.setData(bodyData);
        transferMsg.setContext(ctx);
        MessageGroup.getInstance().pushMessage(transferMsg);
    }

    private void processJson(FullHttpRequest request, int httpIndex, ChannelHandlerContext ctx) {

        QueryStringDecoder decoder = new QueryStringDecoder(request.uri());
        Map<String, String> requestParams = new HashMap<>();
        Map<String, List<String>> parameters = decoder.parameters();
        String path = decoder.path();
        Iterator<Map.Entry<String, List<String>>> iterator = parameters.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, List<String>> next = iterator.next();
            requestParams.put(next.getKey(), next.getValue().get(0));
        }
        String param = request.content().toString(StandardCharsets.UTF_8);

        HeaderProto headerProto = new HeaderProto();
        headerProto.setPlayerIndex(httpIndex);

        TransferMsg transferMsg = new TransferMsg();
        transferMsg.setHeaderProto(headerProto);
        transferMsg.setContext(ctx);
        transferMsg.setParams(requestParams);
        transferMsg.setHttpUrl(path);
        MessageGroup.getInstance().pushMessage(transferMsg);
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.fireChannelActive();
        InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
        String ip = address.getAddress().getHostAddress();
        ctx.channel().attr(Constants.REMOTE_ADDRESS).setIfAbsent(ip);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (ctx.channel().isActive()) {
            ctx.close();
        }
    }
}
