package protocol.remote.login;


import lombok.Getter;
import lombok.Setter;
import protocol.local.common.PlayerSceneProto;

/**
 * Created by Administrator on 2020/7/4.
 */

@Getter
@Setter
public class LoginRsp {
    private String ip;
    private int playerIndex;
    private String name;
}

