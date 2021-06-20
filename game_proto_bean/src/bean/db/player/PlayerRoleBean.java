package bean.db.player;


import core.annotation.redis.Redis;
import core.sql.BaseIntBean;
import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;


@Getter
@Setter
@Table("game_player_role")
@Redis(name = "game_player")
public class PlayerRoleBean extends BaseIntBean {
    @Column()
    private int job;

    @Column()
    private int sex;
}
