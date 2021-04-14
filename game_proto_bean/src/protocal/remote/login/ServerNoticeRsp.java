package protocal.remote.login;

import bean.login.NoticeBean;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ServerNoticeRsp {
    private List<NoticeBean> noticeList;
}
