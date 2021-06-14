package protocal.remote.scene;


import core.annotation.proto.Proto;
import lombok.Getter;
import lombok.Setter;
import protocal.remote.common.Position;

@Getter
@Setter
public class SyncPositionReq {
    private Position position;
    private int sceneId;
}
