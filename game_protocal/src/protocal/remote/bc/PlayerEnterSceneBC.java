package protocal.remote.bc;

import lombok.Getter;
import lombok.Setter;
import protocal.remote.common.Position;

@Getter
@Setter
public class PlayerEnterSceneBC {
    private int playerIndex;
    private Position position;
}
