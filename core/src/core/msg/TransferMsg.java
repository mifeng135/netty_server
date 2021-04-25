package core.msg;

import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import lombok.Setter;
import protocal.TcpHeaderProto;


/**
 * Created by Administrator on 2020/12/5.
 */
@Getter
@Setter
public class TransferMsg {
    private TcpHeaderProto headerProto;
    private byte[] data;
    private ChannelHandlerContext context;
}
