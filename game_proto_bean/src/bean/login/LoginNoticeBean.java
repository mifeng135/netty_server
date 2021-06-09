package bean.login;

import core.annotation.Redis;
import core.sql.BaseIntBean;
import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

/**
 * 登录服务器 通知消息
 */
@Getter
@Setter
@Table("game_notice_list")
@Redis(name = "game_notice_list", IncrName = "id", immediately = true)
public class LoginNoticeBean extends BaseIntBean {
    @Column
    private String content;
}
