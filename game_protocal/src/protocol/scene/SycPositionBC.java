package protocol.scene;

import lombok.Getter;
import lombok.Setter;
import protocol.common.Position;

import java.util.List;


@Getter
@Setter
public class SycPositionBC {
    private Position position;
    private int playerIndex;
    private List<Integer> noticePlayer;
}
