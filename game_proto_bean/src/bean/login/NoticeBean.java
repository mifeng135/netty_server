package bean.login;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class NoticeBean implements Serializable {
    private int noticeId;
    private String content;
}
