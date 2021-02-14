package protocol.local.scene;


import lombok.Getter;
import lombok.Setter;
import protocol.local.base.BaseLocalProto;
import protocol.remote.common.Position;

@Getter
@Setter
public class SycPositionReq extends BaseLocalProto {
    private Position position;
}
