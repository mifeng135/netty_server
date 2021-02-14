package protocol.remote.system;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReconnectReq {
    private int socketIndex;
}
