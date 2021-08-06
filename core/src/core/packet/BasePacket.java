package core.packet;

public interface BasePacket {
    int getPlayerIndex();
    int getMsgId();
    byte getMsgType();
}
