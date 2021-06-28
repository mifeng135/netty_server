package core.netty.tcp;

import core.Constants;
import core.msg.HeaderProto;
import core.msg.TransferMsg;
import core.util.ProtoUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

import static core.Constants.TCP_MSG_LEN;


//-----------------------------------------------------------------------------------------------------------------------
//|              short                             |   short               |  short     | bytes           | bytes       |
//|  msg total len(short short short bytes bytes)  |   header proto len    |  body len  | header bytes    | body bytes  |
//-----------------------------------------------------------------------------------------------------------------------
public class RemoteDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext context, ByteBuf byteBuf, List<Object> list) {
        if (!byteBuf.isReadable()) {
            return;
        }
        byteBuf.markReaderIndex();
        int readLength = byteBuf.readableBytes();
        if (readLength < 2) {
            byteBuf.resetReaderIndex();
            return;
        }
        int msgLength = byteBuf.readShort();//包体消息总长度
        if (readLength < msgLength) {
            byteBuf.resetReaderIndex();
            return;
        }

        short headerLen = byteBuf.readShort();
        short bodyLen = byteBuf.readShort();

        byte[] headerData = new byte[headerLen];
        byteBuf.readBytes(headerData);
        HeaderProto headerProto = ProtoUtil.deserializer(headerData, HeaderProto.class);
        String ip = context.channel().attr(Constants.REMOTE_ADDRESS).get();
        headerProto.setRemoteIp(ip);

        byte[] bodyData = new byte[bodyLen];
        byteBuf.readBytes(bodyData);

        TransferMsg msg = new TransferMsg();
        msg.setData(bodyData);
        msg.setHeaderProto(headerProto);
        msg.setContext(context);
        list.add(msg);
    }
}
