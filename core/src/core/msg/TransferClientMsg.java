package core.msg;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferClientMsg {
    private int msgId;
    private int result;
    private byte[] data;
}
