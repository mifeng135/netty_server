package core.netty.tcp;

import core.msg.TransferMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

import static core.Constants.REMOTE_MSG_DECODER_HEADER_LEN;
import static core.Constants.TCP_HEADER_LEN;

/**
 * Created by Administrator on 2020/12/19.
 */
public class GDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {
        if (!byteBuf.isReadable()) {
            return;
        }
        byteBuf.markReaderIndex();
        int length = byteBuf.readableBytes();
        if (length < TCP_HEADER_LEN) {
            byteBuf.resetReaderIndex();
            return;
        }

        byteBuf.markReaderIndex();
        int msgLength = byteBuf.readShort();
        int readLength = byteBuf.readableBytes();
        if (readLength < msgLength - TCP_HEADER_LEN) {
            byteBuf.resetReaderIndex();
            return;
        }

        int playerIndex = byteBuf.readInt();
        int msgId = byteBuf.readInt();
        byte[] data = new byte[msgLength - REMOTE_MSG_DECODER_HEADER_LEN];
        byteBuf.readBytes(data);

        TransferMsg msg = new TransferMsg();
        msg.setData(data);
        msg.setMsgId(msgId);
        msg.setSocketIndex(playerIndex);
        msg.setPlayerIndex(playerIndex);
        list.add(msg);
    }
}
