package bean.login;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import java.io.Serializable;

@Getter
@Setter
@Table("game_notice_list")
public class NoticeBean implements Serializable {
    @Id
    @Column("notice_id")
    private int noticeId;

    @Column()
    private String content;
}
