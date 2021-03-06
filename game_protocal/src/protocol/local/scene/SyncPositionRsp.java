package protocol.local.scene;

import lombok.Getter;
import lombok.Setter;
import protocol.remote.common.Position;

import java.util.List;


@Setter
@Getter
public class SyncPositionRsp {
    private Position position;
    private boolean move;
    private List<Integer> noticePlayer;
}
