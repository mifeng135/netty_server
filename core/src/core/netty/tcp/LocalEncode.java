package core.netty.tcp;

import core.msg.TransferMsg;
import core.util.ProtoUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import static core.Constants.MSG_TYPE_REMOTE;
import static core.Constants.TCP_MSG_LEN;

/***
 * local encoder
 */

//-----------------------------------------------------------------------------------------------------------------------
//              short                             |   short               |  short     | bytes           | bytes        |
//  msg total len(short short short bytes bytes)  |   header proto len    |  body len  | header bytes    | body bytes   |
//-----------------------------------------------------------------------------------------------------------------------
public class LocalEncode extends MessageToByteEncoder<TransferMsg> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, TransferMsg in, ByteBuf out) {
        in.getHeaderProto().setMsdType(MSG_TYPE_REMOTE);
        byte[] headerData = ProtoUtil.serialize(in.getHeaderProto());
        byte[] bodyData = in.getData() != null ? in.getData() : new byte[0];
        int headerDataLen = headerData.length;
        int bodyDataLen = bodyData.length;
        out.writeShort(TCP_MSG_LEN + headerDataLen + bodyDataLen);
        out.writeShort(headerDataLen);
        out.writeShort(bodyDataLen);
        out.writeBytes(headerData);
        out.writeBytes(bodyData);
    }
}
