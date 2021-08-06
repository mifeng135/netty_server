package core.packet;


import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

import static core.Constants.MSG_TYPE_REMOTE;

public class RemotePacket implements BasePacket {
    public int msgId;
    public int playerIndex;
    public byte[] data;

    public RemotePacket(int msgId, int playerIndex, byte[] data) {
        this.msgId = msgId;
        this.playerIndex = playerIndex;
        this.data = data;
    }

    @Override
    public int getPlayerIndex() {
        return playerIndex;
    }

    @Override
    public int getMsgId() {
        return msgId;
    }

    @Override
    public byte getMsgType() {
        return MSG_TYPE_REMOTE;
    }

    public byte[] getData() {
        return data;
    }

    public <T> T deserializer(Class<T> clazz) {
        Schema<T> schema = RuntimeSchema.getSchema(clazz);
        T message = schema.newMessage();
        ProtobufIOUtil.mergeFrom(data, message, schema);
        return message;
    }

    public <T> byte[] serialize(T obj) {
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
}
