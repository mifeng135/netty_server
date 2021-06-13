package protocal.remote.login;

import bean.login.LoginNoticeBean;
import core.annotation.proto.Proto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Proto
public class ServerNoticeRsp {
    private List<LoginNoticeBean> noticeList;
}
