package bean.login;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import java.io.Serializable;

/**
 * 登录服务器列表
 */


@Getter
@Setter
@Table("game_server_list")
public class ServerListInfoBean implements Serializable {
    @Id
    private int id;

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
}
