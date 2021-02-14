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
    private int msgId; //消息id
    private int playerIndex; //玩家索引
    private byte[] data; // 数据
    private int result; //结果
    private ChannelHandlerContext context;
}
