package protocal.local.system;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RegisterMsgCmdPush {
    List<Integer> msgList;
}
