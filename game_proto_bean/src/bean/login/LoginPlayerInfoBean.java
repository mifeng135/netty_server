package bean.login;

import com.alibaba.fastjson.JSON;
import core.annotation.Redis;
import core.sql.BaseStringBean;
import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;



/***
 * 登录服务器 人物信息
 */
@Getter
@Setter
@Table("game_player_login_info")
@Redis(name = "game_player_login_info", IncrName = "player_index", type = Redis.IncrType.STRING)
public class LoginPlayerInfoBean extends BaseStringBean {
    @Column("player_index")
    private int playerIndex;

    @Column("login_time")
    private int loginTime;

    @Column("player_server_info")
    private String playerServerInfo;

    private transient PlayerServerInfoBean playerServerInfoBean;

    public void decode() {
        playerServerInfoBean = JSON.parseObject(playerServerInfo, PlayerServerInfoBean.class);
    }

    public void encode() {
        playerServerInfo = JSON.toJSONString(playerServerInfoBean);
    }

    @Getter
    @Setter
    public static class PlayerServerInfoBean {
        private int serverId;
        private int playerHead;
    }
}
