package protocol.local.db.login;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class DBLoginReq {
    private String account;
    private String pwd;
}
