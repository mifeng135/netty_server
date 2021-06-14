package protocal.remote.scene;

import core.annotation.proto.Proto;
import lombok.Getter;
import lombok.Setter;
import protocal.remote.common.Position;

import java.util.List;


@Setter
@Getter
public class SyncPositionRsp {
    private Position position;
    private boolean move;
    private List<Integer> noticePlayer;
}
