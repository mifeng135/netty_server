package bean.login;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import java.io.Serializable;

/**
 * 登录服务器 通知消息
 */
@Getter
@Setter
@Table("game_notice_list")
public class LoginNoticeBean implements Serializable {
    @Id
    @Column("notice_id")
    private int noticeId;

    @Column()
    private String content;
}
