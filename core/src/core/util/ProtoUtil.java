package core.util;

import core.msg.TransferMsg;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import protocal.HeaderProto;

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


    public static ByteBuf encodeDBHttpMsg(TransferMsg transferMsg, Object msg) {
        byte[] headerData = ProtoUtil.serialize(transferMsg.getHeaderProto());
        byte[] bodyData = ProtoUtil.serialize(msg);
        byte[] attackData = transferMsg.getAttackData();
        ByteBuf buf = Unpooled.buffer(6 + headerData.length + bodyData.length + attackData.length);
        buf.writeShort(headerData.length);
        buf.writeShort(bodyData.length);
        buf.writeShort(attackData.length);
        buf.writeBytes(headerData);
        buf.writeBytes(bodyData);
        buf.readBytes(attackData);
        return buf;
    }

    public static TransferMsg decodeDBHttpMsg(byte[] data) {

        ByteBuf buf = Unpooled.wrappedBuffer(data);

        int headerLen = buf.readShort();
        int bodyLen = buf.readShort();
        int attackLen = buf.readShort();

        byte[] headerData = new byte[headerLen];
        buf.readBytes(headerData);

        byte[] bodyData = new byte[bodyLen];
        buf.readBytes(bodyData);

        byte[] attackData = new byte[attackLen];
        buf.readBytes(bodyData);

        HeaderProto headerProto = ProtoUtil.deserializer(headerData, HeaderProto.class);
        TransferMsg transferMsg = new TransferMsg();
        transferMsg.setHeaderProto(headerProto);
        transferMsg.setData(bodyData);
        transferMsg.setAttackData(attackData);
        return transferMsg;
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
