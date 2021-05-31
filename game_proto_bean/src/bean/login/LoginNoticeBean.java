package bean.login;

import core.annotation.Redis;
import core.annotation.RedisId;
import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import java.io.Serializable;

import static constants.RedisConstant.REDIS_SERVER_NOTICE_KEY;

/**
 * 登录服务器 通知消息
 */
@Getter
@Setter
@Table("game_notice_list")
@Redis(name = "game_notice_list", key = REDIS_SERVER_NOTICE_KEY)
public class LoginNoticeBean implements Serializable {
    @Id
    @Column("notice_id")
    @RedisId
    private int noticeId;

    @Column()
    private String content;

    @Column
    private byte[] item;
}
