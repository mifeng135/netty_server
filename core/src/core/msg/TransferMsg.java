package core.msg;

import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import lombok.Setter;
import protocal.HeaderProto;


/**
 * Created by Administrator on 2020/12/5.
 */
@Getter
@Setter
public class TransferMsg {
    private HeaderProto headerProto;
    private byte[] data;
    private ChannelHandlerContext context;
}
