package protocol.local.scene;


import lombok.Getter;
import lombok.Setter;
import protocol.remote.common.Position;

@Getter
@Setter
public class SyncPositionReq {
    private Position position;
    private int sceneId;
}
