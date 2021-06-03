package core.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sun.org.apache.bcel.internal.generic.ARETURN;
import core.util.ProtoUtil;
import io.netty.buffer.*;
import org.redisson.client.codec.BaseCodec;
import org.redisson.client.protocol.Decoder;
import org.redisson.client.protocol.Encoder;


public class FastJsonCodec extends BaseCodec {

    private final Encoder encoder = in -> {
        ByteBuf out = ByteBufAllocator.DEFAULT.buffer();
        try {
            ByteBufOutputStream os = new ByteBufOutputStream(out);
            JSON.writeJSONString(os, in, SerializerFeature.WriteClassName);
            return os.buffer();
        } catch (Exception e) {
            out.release();
            throw e;
        }
    };
    private final Decoder<Object> decoder = (buf, state) -> {
        Object oc = JSON.parseObject(new ByteBufInputStream(buf), Object.class);
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

    static {
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
    }
}
