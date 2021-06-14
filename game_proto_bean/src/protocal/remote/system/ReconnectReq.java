package protocal.remote.system;


import core.annotation.proto.Proto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReconnectReq {
    private int socketIndex;
}
