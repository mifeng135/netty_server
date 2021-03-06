package core.netty.tcp;

import core.msg.TransferClientMsg;
import core.msg.TransferMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import static core.Constants.REMOTE_MSG_ENCODER_HEADER_LEN;

/**
 * Created by Administrator on 2020/12/19.
 */
public class GEncoder extends MessageToByteEncoder<TransferClientMsg> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, TransferClientMsg in, ByteBuf out) {
        out.writeShort(REMOTE_MSG_ENCODER_HEADER_LEN + in.getData().length);
        out.writeInt(in.getMsgId());
        out.writeShort(in.getResult());
        out.writeBytes(in.getData());
    }
}
