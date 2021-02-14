package protocol.local.center;

import lombok.Getter;
import lombok.Setter;
import protocol.local.base.BaseLocalProto;

@Getter
@Setter
public class CenterSessionReq extends BaseLocalProto {
    private int state; //1  链接成功 0 关闭
}
