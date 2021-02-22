package protocol.local.gate;

import lombok.Getter;
import lombok.Setter;
import protocol.local.base.BaseLocalProto;
import protocol.local.common.PlayerSceneProto;

@Getter
@Setter
public class PlayerEnterSceneRsp extends BaseLocalProto {
    private PlayerSceneProto playerSceneProto;
}
