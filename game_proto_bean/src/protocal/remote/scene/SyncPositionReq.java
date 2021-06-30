package protocal.remote.scene;


import lombok.Getter;
import lombok.Setter;
import protocal.remote.common.Position;

@Getter
@Setter
public class SyncPositionReq {
    private Position position;
    private float moveAngle;
    private float delayTime;
    private boolean firstMove;
}
