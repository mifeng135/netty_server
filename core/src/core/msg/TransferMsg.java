package core.msg;

import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

import static core.Constants.HTTP_DECODER_TYPE_JSON;
import static core.Constants.HTTP_DECODER_TYPE_PROTO_BUFFER;


/**
 * Created by Administrator on 2020/12/5.
 */
@Getter
@Setter
public class TransferMsg {
    private HeaderProto headerProto;
    private byte[] data;
    private transient ChannelHandlerContext context;
    private transient Map<String, String> params;
    private transient String httpUrl;

    public int getMsgType() {
        if (httpUrl != null && httpUrl.length() > 0) {
            return HTTP_DECODER_TYPE_JSON;
        }
        return HTTP_DECODER_TYPE_PROTO_BUFFER;
    }
}
