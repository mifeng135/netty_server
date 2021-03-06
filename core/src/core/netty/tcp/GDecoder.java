package core.netty.tcp;

import core.msg.TransferMsg;
import core.util.ProtoUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import protocol.local.base.HeaderProto;

import java.util.List;

import static core.Constants.TCP_MSG_LEN;

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
        if (length < TCP_MSG_LEN) {
            byteBuf.resetReaderIndex();
            return;
        }

        byteBuf.markReaderIndex();
        int msgLength = byteBuf.readShort();
        int readLength = byteBuf.readableBytes();
        if (readLength < msgLength - TCP_MSG_LEN) {
            byteBuf.resetReaderIndex();
            return;
        }

        short headerLen = byteBuf.readShort();
        short bodyLen = byteBuf.readShort();


        byte[] headerData = new byte[headerLen];
        byteBuf.readBytes(headerData);
        HeaderProto headerProto = ProtoUtil.deserializer(headerData, HeaderProto.class);

        byte[] bodyData = new byte[bodyLen];
        byteBuf.readBytes(bodyData);

        TransferMsg msg = new TransferMsg();
        msg.setData(bodyData);
        msg.setHeaderProto(headerProto);
        list.add(msg);
    }
}
