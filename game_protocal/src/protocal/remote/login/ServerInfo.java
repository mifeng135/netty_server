package protocal.remote.login;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServerInfo {
    private String serverName;
    private int serverId;
    private int state;
    private String serverIp;
    private int openTime;
}
