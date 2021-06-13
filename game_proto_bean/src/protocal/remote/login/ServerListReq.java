package protocal.remote.login;

import core.annotation.proto.Proto;
import lombok.Getter;

@Getter
@Proto
public class ServerListReq {
    private String openId;
}
