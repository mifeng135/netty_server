package protocol.local.db;


import lombok.Getter;
import lombok.Setter;
import protocol.local.base.BaseDBLocalProto;

@Getter
@Setter
public class PlayerInfoLoginRsp extends BaseDBLocalProto {
    private String name;
    private int id;
    private int result;
}
