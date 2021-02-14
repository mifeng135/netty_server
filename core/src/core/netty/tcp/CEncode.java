package core.netty.tcp;

import core.msg.TransferMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import static core.Constants.LOCAL_MSG_ENCODER_HEADER_LEN;

public class CEncode extends MessageToByteEncoder<TransferMsg> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, TransferMsg in, ByteBuf out) {
        out.writeShort(LOCAL_MSG_ENCODER_HEADER_LEN + in.getData().length);
        out.writeInt(in.getMsgId());
        out.writeBytes(in.getData());
    }
}
