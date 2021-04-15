package protocal.remote.login;

import bean.login.ServerInfoBean;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetServerListRsp {
    private List<ServerInfoBean> serverList;
    private List<ServerInfoBean> selfServerList;
    private int playerIndex;
}
