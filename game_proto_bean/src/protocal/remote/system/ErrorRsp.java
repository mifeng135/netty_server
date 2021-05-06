package protocal.remote.system;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorRsp {
    private int errorCode;
    private int msgId;
}
