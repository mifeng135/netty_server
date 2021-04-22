package bean.login;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import java.io.Serializable;

/***
 * 人物相关登录服务器信息
 */

@Getter
@Setter
@Table("game_player_server_info")
public class LoginPlayerServerInfoBean implements Serializable {
    @Id
    @Column("player_index")
    private int playerIndex;

    @Column("server_id")
    private int serverId;
}
