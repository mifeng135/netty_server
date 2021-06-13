package protocal.remote.system;

import core.annotation.proto.Proto;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Administrator on 2020/7/4.
 */
@Getter
@Setter
@Proto
public class ClientSocketLoginReq {
    private int playerIndex;
}
