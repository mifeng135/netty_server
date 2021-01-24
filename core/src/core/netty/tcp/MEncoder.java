package core.netty.tcp;

import core.msg.TransferMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import static core.Constants.TCP_HEADER_LEN;

/**
 * Created by Administrator on 2020/12/19.
 */
public class MEncoder extends MessageToByteEncoder<TransferMsg> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, TransferMsg in, ByteBuf out) {
        out.writeShort(TCP_HEADER_LEN + 6 + in.getData().length);
        out.writeInt(in.getMsgId());
        out.writeShort(in.getResult());
        out.writeBytes(in.getData());
    }
}
