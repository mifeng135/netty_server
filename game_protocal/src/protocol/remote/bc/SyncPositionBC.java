package protocol.remote.bc;

import lombok.Getter;
import lombok.Setter;
import protocol.remote.common.Position;


@Getter
@Setter
public class SyncPositionBC {
    private Position position;
    private int playerIndex;
}
