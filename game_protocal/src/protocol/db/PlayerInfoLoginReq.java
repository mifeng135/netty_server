package protocol.db;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PlayerInfoLoginReq extends BaseDB {
    private String account;
    private String pwd;
}
