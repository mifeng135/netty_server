package core.netty.tcp;

import core.msg.HeaderProto;
import core.msg.TransferMsg;
import core.util.ProtoUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

import static core.Constants.TCP_MSG_LEN;


/***
 * local decoder
 */

//-----------------------------------------------------------------------------------------------------------------------
//|              short                             |   short               |  short     | bytes           | bytes       |
//|  msg total len(short short short bytes bytes)  |   header proto len    |  body len  | header bytes    | body bytes  |
//-----------------------------------------------------------------------------------------------------------------------
public class LocalDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {
        if (!byteBuf.isReadable()) {
            return;
        }
        byteBuf.markReaderIndex();
        int readLength = byteBuf.readableBytes();// 获取可读字节长度
        if (readLength < 2) {
            byteBuf.resetReaderIndex();
            return;
        }

        int msgLength = byteBuf.readShort(); //消息总长度
        if (readLength < msgLength) {
            byteBuf.resetReaderIndex();
            return;
        }
        short headerLen = byteBuf.readShort(); //proto header len
        short bodyLen = byteBuf.readShort(); // proto body len

        byte[] headerData = new byte[headerLen];
        byteBuf.readBytes(headerData);
        HeaderProto headerProto = ProtoUtil.deserializer(headerData, HeaderProto.class);

        byte[] bodyData = new byte[bodyLen];
        byteBuf.readBytes(bodyData);

        TransferMsg msg = new TransferMsg();
        msg.setData(bodyData);
        msg.setContext(channelHandlerContext);
        msg.setHeaderProto(headerProto);
        list.add(msg);
    }
}
