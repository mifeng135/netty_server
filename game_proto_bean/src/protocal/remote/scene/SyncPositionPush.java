package protocal.remote.scene;

import lombok.Getter;
import lombok.Setter;
import protocal.remote.common.Position;


@Setter
@Getter
public class SyncPositionPush {
    private int playerIndex;
    private Position position;
    private boolean firstMove;
}
