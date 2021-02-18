package protocol.local.db.login;

import lombok.Getter;
import lombok.Setter;
import protocol.local.base.BaseDBLocalProto;

@Getter
@Setter
public class DBLoginRsp extends BaseDBLocalProto {
    private int id;
    private String name;
    private int result;
}
