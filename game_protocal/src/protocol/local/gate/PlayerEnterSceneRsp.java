package protocol.local.gate;

import lombok.Getter;
import lombok.Setter;
import protocol.local.common.PlayerSceneProto;

@Getter
@Setter
public class PlayerEnterSceneRsp {
    private PlayerSceneProto playerSceneProto;
}
