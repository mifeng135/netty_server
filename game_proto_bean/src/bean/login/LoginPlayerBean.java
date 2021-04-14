package bean.login;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class LoginPlayerBean implements Serializable {
    private int playerIndex;
    private String serverInfo;
}
