package protocol.system;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErroRsp {
    private int msgId;
    private String errorStr;
}
