package protocol.scene;


import lombok.Getter;
import lombok.Setter;
import protocol.common.Position;

@Getter
@Setter
public class SycPositionReq {
    private Position position;
    private int playerIndex;
}
