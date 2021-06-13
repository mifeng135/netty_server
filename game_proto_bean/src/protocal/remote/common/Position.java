package protocal.remote.common;


import core.annotation.proto.Proto;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Proto
public class Position {
    private int x;
    private int y;
}
