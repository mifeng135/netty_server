package protocol.login;

import lombok.Getter;

@Getter
public class RegisterReq {
    private String account;
    private String pwd;
    private String confirmPwd;
    private String nickName;
}
