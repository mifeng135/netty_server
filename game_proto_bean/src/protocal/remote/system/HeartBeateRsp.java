package protocal.remote.system;

import core.annotation.proto.Proto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Proto
public class HeartBeateRsp {
    private int time;
}
