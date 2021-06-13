package bean.login;

import core.annotation.Redis;
import core.sql.BaseIntBean;
import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;


/**
 * 登录服务器列表
 */

@Getter
@Setter
@Table("game_server_list")
@Redis(name = "game_server_list", IncrName = "id", immediately = true)
public class ServerListInfoBean extends BaseIntBean {
    @Column("server_name")
    private String serverName;

    @Column("server_id")
    private int serverId;

    @Column("state")
    private int state;

    @Column("server_ip")
    private String serverIp;

    @Column("open_time")
    private int openTime;

    @Column("group_id")
    private int groupId;
}
