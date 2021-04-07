package protocal.local.system;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class TcpRsp {
    List<Integer> msgList;
}
