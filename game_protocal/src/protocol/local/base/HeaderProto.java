package protocol.local.base;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Setter
@Getter
public class HeaderProto {
    private int playerIndex;
    private int msgId;
    private int msdType; // local 0 remote 1
    private boolean broadcast;
    private boolean result = true;
    private List<Integer> noticeList;
}
