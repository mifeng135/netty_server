package core.msg;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Setter
@Getter
public class HeaderProto {
    private int playerIndex;
    private int msgId;
    private int msdType; // local 0 remote 1

    //only local server use
    private boolean broadcast;
    private boolean success = true;
    private String remoteIp;
    private List<Integer> noticeList;
}
