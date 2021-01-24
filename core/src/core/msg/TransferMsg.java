package core.msg;

import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import lombok.Setter;


/**
 * Created by Administrator on 2020/12/5.
 */
@Getter
@Setter
public class TransferMsg {
    private int msgId;
    private int socketIndex;
    private byte[] data;
    private int result;
    private ChannelHandlerContext context;
}
