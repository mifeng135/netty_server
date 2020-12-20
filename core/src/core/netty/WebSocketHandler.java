package core.netty;

import core.Configs;
import core.manager.WebSocketConnectionManager;
import core.proto.TransferMsg;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import org.apache.log4j.Logger;


/**
 * Created by Administrator on 2020/5/28.
 */
public abstract class WebSocketHandler extends ChannelInboundHandlerAdapter {

    private static Logger logger = Logger.getLogger(WebSocketHandler.class);

    private WebSocketServerHandshaker handShaker;

    public WebSocketHandler() {
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!ctx.channel().isActive()) {
            return;
        }
        if (msg instanceof FullHttpRequest) {
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        } else if (msg instanceof WebSocketFrame) {
            handleWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }

    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
        if (frame instanceof CloseWebSocketFrame) {
            handShaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            return;
        }
        if (frame instanceof BinaryWebSocketFrame) {


            ByteBuf buf = frame.content();
            int msgId = buf.readInt();
            int dataLength = buf.readInt();
            byte[] data = new byte[dataLength];
            buf.readBytes(data);
//
//            if (swallowDispatchMsg(ctx, transferMsg)) {
//                return;
//            }
//            int playerIndex = ctx.channel().attr(Configs.PLAYER_INDEX).get();
//            transferMsg.setPlayerIndex(playerIndex);
//            dispatchMsg(transferMsg);
        }
    }


    // process client over time
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            ctx.close();
            WebSocketConnectionManager.mfdChannelGroup.remove(ctx.channel().attr(Configs.PLAYER_INDEX).get());
            channelClose(ctx);
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        WebSocketConnectionManager.mClientChannelGroup.add(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        WebSocketConnectionManager.mfdChannelGroup.remove(ctx.channel().attr(Configs.PLAYER_INDEX).get());
        channelClose(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        ctx.close();
    }

    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
        if (!req.decoderResult().isSuccess()) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }
        if (req.method() != HttpMethod.GET) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN));
            return;
        }
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(getWebSocketLocation(req), null, true, 5 * 1024 * 1024);
        handShaker = wsFactory.newHandshaker(req);
        if (handShaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            handShaker.handshake(ctx.channel(), req);
        }
    }

    private void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse res) {
        if (res.status().code() != HttpResponseStatus.OK.code()) {
            ByteBuf buf = Unpooled.copiedBuffer(res.status().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
            HttpUtil.setContentLength(res, res.content().readableBytes());
        }
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!HttpUtil.isKeepAlive(req) || res.status().code() != HttpResponseStatus.OK.code()) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    private String getWebSocketLocation(FullHttpRequest req) {
        String location = req.headers().get("Host") + "/websocket";
        return "ws://" + location;
    }

    public abstract boolean swallowDispatchMsg(ChannelHandlerContext context, TransferMsg msgBean);

    public abstract void dispatchMsg(TransferMsg msgBean);

    public abstract void channelClose(ChannelHandlerContext context);

}
