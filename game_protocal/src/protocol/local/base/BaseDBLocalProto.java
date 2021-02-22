package protocol.local.base;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BaseDBLocalProto extends BaseLocalProto {
    private int queryPlayerIndex; //要查询人物信息的id
    private int queryMsgId; // 要请求消息的消息id
    private int result;
}
