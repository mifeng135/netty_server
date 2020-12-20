package core.proto;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Created by Administrator on 2020/12/19.
 */
public class MDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (!byteBuf.isReadable()) {
            return;
        }
        byteBuf.markReaderIndex();
        int length = byteBuf.readableBytes();
        if(length < 2) {
            byteBuf.resetReaderIndex();
            return;
        }

        byteBuf.markReaderIndex();
        int msgLength = byteBuf.readShort();
        int readLength = byteBuf.readableBytes();
        if(readLength < msgLength - 2) {
            byteBuf.resetReaderIndex();
            return;
        }

        int playerIndex = byteBuf.readInt();
        int msgId = byteBuf.readInt();
        byte[] data = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(data);

        TransferMsg msg = new TransferMsg();
        msg.setData(data);
        msg.setMsgId(msgId);
        msg.setPlayerIndex(playerIndex);
        list.add(msg);
    }
}
