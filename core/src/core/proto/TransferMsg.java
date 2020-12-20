package core.proto;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.protostuff.LinkedBuffer;
import lombok.Getter;
import lombok.Setter;


/**
 * Created by Administrator on 2020/12/5.
 */
@Getter
@Setter
public class TransferMsg {
    private int msgId;
    private String socketKey;
    private int playerIndex;
    private byte[] data;

    /**
     * 构建http到客户端都消息包
     *
     * @return
     */
    public ByteBuf packClientMsg() {
        ByteBuf buf = Unpooled.buffer(8 + data.length);
        buf.writeInt(msgId);
        buf.writeInt(data.length);
        buf.writeBytes(data);
        return buf;
    }

    public byte[] packServerMsg() {
        return ProtoUtil.serialize(this);
    }
}
