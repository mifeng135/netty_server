package protocal.remote.login;

import bean.login.ServerListInfoBean;
import bean.sub.SubPlayerServerInfoBean;
import core.annotation.proto.Proto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ServerListRsp {
    private List<ServerListInfoBean> serverList;
    private List<SubPlayerServerInfoBean> selfServerList;
    private int playerIndex;
}
