package core.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sun.org.apache.bcel.internal.generic.ARETURN;
import core.util.ProtoUtil;
import io.netty.buffer.*;
import org.redisson.client.codec.BaseCodec;
import org.redisson.client.protocol.Decoder;
import org.redisson.client.protocol.Encoder;


public class FastJsonCodec extends BaseCodec {

    private final Encoder encoder = in -> {
        byte[] data = ProtoUtil.serialize(in);
        ByteBuf byteBuf = Unpooled.buffer(data.length);
        byteBuf.writeBytes(data);
        return byteBuf;
    };
    private final Decoder<Object> decoder = (buf, state) -> {
        Object oc = ProtoUtil.deserializer(buf.array(), Object.class);
        return oc;
    };

    @Override
    public Decoder<Object> getValueDecoder() {
        return decoder;
    }

    @Override
    public Encoder getValueEncoder() {
        return encoder;
    }

}
