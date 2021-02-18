package protocol.local.db.login;

import lombok.Getter;
import lombok.Setter;
import protocol.local.base.BaseDBLocalProto;


@Setter
@Getter
public class DBLoginReq extends BaseDBLocalProto {
    private String account;
    private String pwd;
}
