package protocol.local.db.scene;

import lombok.Getter;
import lombok.Setter;
import protocol.local.base.BaseDBLocalProto;

@Setter
@Getter
public class DBSceneRsp extends BaseDBLocalProto {
    private float positionX;
    private float positionY;
    private int sceneId;
}
