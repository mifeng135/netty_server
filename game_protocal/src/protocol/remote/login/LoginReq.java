package protocol.remote.login;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Administrator on 2020/7/4.
 */

@Getter
@Setter
public class LoginReq {
    private String account;
    private String password;
}
