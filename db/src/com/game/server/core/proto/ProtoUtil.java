package com.game.server.core.proto;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

/**
 * Created by Administrator on 2020/7/4.
 */
public class ProtoUtil {

    private static LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);

    public static <T> byte[] serialize(T obj) {
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
}
