package com.game.server.core.netty;

import com.game.server.core.config.Configs;
import com.game.server.core.connect.ConnectionManager;
import com.game.server.core.msg.MsgBean;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;
import org.apache.log4j.Logger;


/**
 * Created by Administrator on 2020/5/28.
 */

public abstract class ServerHandler extends ChannelInboundHandlerAdapter {

    private static Logger logger = Logger.getLogger(ServerHandler.class);

    private WebSocketServerHandshaker handShaker;

    public ServerHandler() {
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
            int cmd = buf.readInt();
            int dataLength = buf.readInt();
            byte[] data = new byte[dataLength];
            buf.readBytes(data);

            MsgBean msgBean = new MsgBean();
            msgBean.setCmd(cmd);
            msgBean.setData(data);

            if (swallowDispatchMsg(ctx, msgBean)) {
                return;
            }
            int playerIndex = ctx.channel().attr(Configs.PLAYER_INDEX).get();
            msgBean.setId(playerIndex);
            dispatchMsg(msgBean);
        }
    }


    // process client over time
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateHandler) {
            ctx.close();
            ConnectionManager.mfdChannelGroup.remove(ctx.channel().attr(Configs.PLAYER_INDEX).get());
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        ConnectionManager.mClientChannelGroup.add(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        ConnectionManager.mfdChannelGroup.remove(ctx.channel().attr(Configs.PLAYER_INDEX).get());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        ctx.close();
    }

    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
        if (!req.getDecoderResult().isSuccess()) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }
        if (req.getMethod() != HttpMethod.GET) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN));
            return;
        }
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(getWebSocketLocation(req), null, true, 5 * 1024 * 1024);
        handShaker = wsFactory.newHandshaker(req);
        if (handShaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedWebSocketVersionResponse(ctx.channel());
        } else {
            handShaker.handshake(ctx.channel(), req);
        }
    }

    private void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse res) {
        if (res.getStatus().code() != HttpResponseStatus.OK.code()) {
            ByteBuf buf = Unpooled.copiedBuffer(res.getStatus().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
            HttpHeaders.setContentLength(res, res.content().readableBytes());
        }
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!HttpHeaders.isKeepAlive(req) || res.getStatus().code() != HttpResponseStatus.OK.code()) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    private String getWebSocketLocation(FullHttpRequest req) {
        String location = req.headers().get(HttpHeaders.Names.HOST) + "/websocket";
        return "ws://" + location;
    }

    public abstract boolean swallowDispatchMsg(ChannelHandlerContext context, MsgBean msgBean);

    public abstract void dispatchMsg(MsgBean msgBean);
}
