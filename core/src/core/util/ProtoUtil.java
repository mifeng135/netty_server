package core.util;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import protocal.local.base.HeaderProto;

import java.util.List;

/**
 * Created by Administrator on 2020/7/4.
 */
public class ProtoUtil {

    public static <T> byte[] serialize(T obj) {
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        Schema schema = RuntimeSchema.getSchema(obj.getClass());
        byte[] data;
        try {
            data = ProtobufIOUtil.toByteArray(obj, schema, buffer);
        } finally {
            buffer.clear();
        }
        return data;
    }

    public static <T> T deserializer(byte[] bytes, Class<T> clazz) {
        Schema<T> schema = RuntimeSchema.getSchema(clazz);
        T message = schema.newMessage();
        ProtobufIOUtil.mergeFrom(bytes, message, schema);
        return message;
    }

    public static HeaderProto initHeaderProto(int msgId, int playerIndex, List<Integer> noticePlayer) {
        HeaderProto headerProto = new HeaderProto();
        headerProto.setMsgId(msgId);
        headerProto.setPlayerIndex(playerIndex);
        headerProto.setNoticeList(noticePlayer);
        return headerProto;
    }

    public static HeaderProto initHeaderProto(int msgId, int playerIndex) {
        HeaderProto headerProto = new HeaderProto();
        headerProto.setMsgId(msgId);
        headerProto.setPlayerIndex(playerIndex);
        return headerProto;
    }
}
