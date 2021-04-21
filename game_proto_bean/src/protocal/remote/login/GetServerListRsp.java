package protocal.remote.login;

import bean.login.ServerListInfoBean;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetServerListRsp {
    private List<ServerListInfoBean> serverList;
    private List<ServerListInfoBean> selfServerList;
}
