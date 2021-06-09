package bean.player;


import core.sql.BaseIntBean;
import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;


@Getter
@Setter
@Table("game_player_role")
public class PlayerRoleBean extends BaseIntBean {
    @Column()
    private int job;

    @Column()
    private int sex;
}
