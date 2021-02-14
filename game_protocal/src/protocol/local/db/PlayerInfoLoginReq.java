package protocol.local.db;


import lombok.Getter;
import lombok.Setter;
import protocol.local.base.BaseDBLocalProto;

@Setter
@Getter
public class PlayerInfoLoginReq extends BaseDBLocalProto {
    private String account;
    private String pwd;
}
