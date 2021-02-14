package protocol.local.scene;

import lombok.Getter;
import lombok.Setter;
import protocol.local.base.BaseLocalProto;
import protocol.remote.common.Position;


@Setter
@Getter
public class SycPositionRsp extends BaseLocalProto {
    private Position position;
    private boolean move;
}
