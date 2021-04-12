package protocal.remote.login;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetServerListRsp {
    private List<ServerInfo> serverList;
    private List<ServerInfo> selfServerList;
}
