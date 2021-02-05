package protocol.system;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CenterSessionReq {
    private int playerIndex;
    private int state; //1  链接成功 0 关闭
}
