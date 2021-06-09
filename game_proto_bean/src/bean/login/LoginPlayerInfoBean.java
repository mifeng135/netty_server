package bean.login;

import bean.sub.SubPlayerServerInfoBean;
import com.alibaba.fastjson.JSON;
import core.annotation.Redis;
import core.sql.BaseStringBean;
import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/***
 * 登录服务器 人物信息
 */
@Getter
@Setter
@Table("game_player_login_info")
@Redis(name = "game_player_login_info", IncrName = "player_index", immediately = true)
public class LoginPlayerInfoBean extends BaseStringBean {
    @Column("player_index")
    private int playerIndex;

    @Column("create_time")
    private int createTime;

    @Column("player_server_info")
    private String playerServerInfo;

    public Map decode() {
        return JSON.parseObject(playerServerInfo, ConcurrentHashMap.class);
    }

    public void encode(Map<Integer, SubPlayerServerInfoBean> data) {
        playerServerInfo = JSON.toJSONString(data);
    }
}
