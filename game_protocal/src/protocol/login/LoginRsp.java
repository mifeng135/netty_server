package protocol.login;


import lombok.Getter;
import lombok.Setter;

/**
 * Created by Administrator on 2020/7/4.
 */

@Getter
@Setter
public class LoginRsp {
    private String ip;
    private int playerIndex;
    private String name;
}

