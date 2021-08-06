package core.packet;

import static core.Constants.MSG_TYPE_LOCAL;

public class LocalDBPacket implements BasePacket {
    private int msgId;
    private int tempId;
    private int playerIndex;
    private byte[] data;
    private byte[] tempData;

    public LocalDBPacket(int msgId, int tempId, int playerIndex, byte[] data, byte[] tempData) {
        this.msgId = msgId;
        this.tempId = tempId;
        this.playerIndex = playerIndex;
        this.data = data;
        this.tempData = tempData;
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
        return MSG_TYPE_LOCAL;
    }

    public byte[] getData() {
        return data;
    }

    public byte[] getTempData() {
        return tempData;
    }

    public int getTempId() {
        return tempId;
    }
}
