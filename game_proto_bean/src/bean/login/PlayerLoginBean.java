package bean.login;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Table;

import java.io.Serializable;

@Getter
@Setter
@Table("game_player_login_info")
public class PlayerLoginBean implements Serializable {
    @Name()
    @Column("open_id")
    private String openId;

    @Id
    @Column("player_index")
    private int playerIndex;

    @Column("login_time")
    private int loginTime;
}
