package protocal.push;

import lombok.Getter;
import lombok.Setter;
import protocal.remote.common.Position;


@Getter
@Setter
public class SyncPositionPush {
    private int playerIndex;
    private Position position;
    private float moveAngle;
    private float delayTime;
}
