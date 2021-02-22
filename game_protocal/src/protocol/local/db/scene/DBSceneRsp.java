package protocol.local.db.scene;

import lombok.Getter;
import lombok.Setter;
import protocol.local.base.BaseDBLocalProto;
import protocol.local.common.PlayerSceneProto;

@Setter
@Getter
public class DBSceneRsp extends BaseDBLocalProto {
    private PlayerSceneProto playerSceneProto;
}
