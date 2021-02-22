package protocol.remote.bc;

import lombok.Getter;
import lombok.Setter;
import protocol.remote.common.Position;

@Getter
@Setter
public class PlayerEnterSceneBC {
    private int playerIndex;
    private Position position;
}
