package protocol.scene;

import lombok.Getter;
import lombok.Setter;
import protocol.common.Position;


@Setter
@Getter
public class SycPositionRsp {
    private Position position;
    private boolean move;
    private int playerIndex;
}
