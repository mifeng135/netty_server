package protocol.local.scene;

import lombok.Getter;
import lombok.Setter;
import protocol.local.base.BaseLocalProto;

@Getter
@Setter
public class EnterSceneReq extends BaseLocalProto {
    private int sceneId;
}
