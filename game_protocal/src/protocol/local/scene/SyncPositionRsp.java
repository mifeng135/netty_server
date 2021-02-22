package protocol.local.scene;

import lombok.Getter;
import lombok.Setter;
import protocol.local.base.BaseLocalProto;
import protocol.remote.common.Position;

import java.util.List;


@Setter
@Getter
public class SyncPositionRsp extends BaseLocalProto {
    private Position position;
    private boolean move;
    private List<Integer> noticePlayer;
}
