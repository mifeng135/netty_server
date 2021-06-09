package bean.player;


import core.sql.BaseIntBean;
import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;


/**
 * Created by Administrator on 2020/6/5.
 */

@Getter
@Setter
@Table("game_player")
public class PlayerInfoBean extends BaseIntBean {
    @Column
    private String name;

    @Column("register_time")
    private int registerTime;

    @Column("login_ip")
    private String loginIp;

    @Column("last_login_time")
    private int lastLoginTime;

    @Column
    private String header;

    @Column("open_id")
    private String openId;

    @Column("server_id")
    private int serverId;
}
